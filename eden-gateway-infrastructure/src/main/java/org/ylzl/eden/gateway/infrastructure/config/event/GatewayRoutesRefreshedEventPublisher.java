package org.ylzl.eden.gateway.infrastructure.config.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.ylzl.eden.gateway.infrastructure.config.env.GatewayRoutesRefreshProperties;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 网关路由刷新事件发布
 *
 * @author gyl
 * @since 2.4.x
 */
@ConditionalOnProperty(GatewayRoutesRefreshProperties.PREFIX)
@Slf4j
@Component
public class GatewayRoutesRefreshedEventPublisher implements ApplicationEventPublisherAware {

	private final RouteDefinitionWriter routeDefinitionWriter;
	private final RouteDefinitionLocator routeDefinitionLocator;
	private ApplicationEventPublisher publisher;

	public GatewayRoutesRefreshedEventPublisher(RouteDefinitionWriter routeDefinitionWriter, RouteDefinitionLocator routeDefinitionLocator) {
		this.routeDefinitionWriter = routeDefinitionWriter;
		this.routeDefinitionLocator = routeDefinitionLocator;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	public void add(RouteDefinition routeDefinition) {
		log.info("Gateway add route: {}", routeDefinition);
		routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
		publisher.publishEvent(new RefreshRoutesEvent(this));
	}

	public void delete(RouteDefinition routeDefinition) {
		log.info("Gateway delete route: {}", routeDefinition);
		routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
		publisher.publishEvent(new RefreshRoutesEvent(this));
	}

	public void update(RouteDefinition routeDefinition) {
		log.info("Gateway update route: {}", routeDefinition);
		routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
		routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
		publisher.publishEvent(new RefreshRoutesEvent(this));
	}

	public void update(List<RouteDefinition> routeDefinitions) {
		this.clearOldRouteDefinitions();
		routeDefinitions.forEach(e -> {
			log.info("Gateway batch add route: {}", e);
			routeDefinitionWriter.save(Mono.just(e)).subscribe();
		});
		publisher.publishEvent(new RefreshRoutesEvent(this));
	}

	private void clearOldRouteDefinitions() {
		List<RouteDefinition> oldRouteDefinitions =
			routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
		if (!CollectionUtils.isEmpty(oldRouteDefinitions)) {
			oldRouteDefinitions.forEach(e -> {
				log.info("Gateway batch delete route: {}", e);
				routeDefinitionWriter.delete(Mono.just(e.getId()));
			});
		}
	}
}

package org.ylzl.eden.gateway.infrastructure.config.autoconfigure;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.ylzl.eden.spring.cloud.sentinel.web.BlockResponseBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Sentinel 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SentinelAutoConfiguration {

	private final List<ViewResolver> viewResolvers;
	private final ServerCodecConfigurer serverCodecConfigurer;

	public SentinelAutoConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
								ServerCodecConfigurer serverCodecConfigurer) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		GatewayCallbackManager.setBlockHandler((exchange, t) -> ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(BlockResponseBuilder.buildResponse(t))));
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
	}

	@Order(-1)
	@Bean
	public GlobalFilter globalFilter() {
		return new SentinelGatewayFilter();
	}
}

package org.ylzl.eden.gateway.infrastructure.config.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.gateway.infrastructure.config.memory.FixedInMemoryRouteDefinitionRepository;

/**
 * 修复内存路由定义仓库自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class FixedInMemoryRouteAutoConfiguration {

	@Bean
	public RouteDefinitionRepository inMemoryRouteDefinitionRepository() {
		log.debug("Autowired FixedInMemoryRouteDefinitionRepository");
		return new FixedInMemoryRouteDefinitionRepository();
	}
}

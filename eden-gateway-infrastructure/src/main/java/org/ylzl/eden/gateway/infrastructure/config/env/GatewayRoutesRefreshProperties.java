package org.ylzl.eden.gateway.infrastructure.config.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网关路由刷新配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = GatewayRoutesRefreshProperties.PREFIX)
public class GatewayRoutesRefreshProperties {

	public static final String PREFIX = "spring.cloud.gateway.refresh-routes";

	private final Nacos nacos = new Nacos();

	@Data
	public static class Nacos {

		private boolean enabled;

		private String dataId;
	}
}

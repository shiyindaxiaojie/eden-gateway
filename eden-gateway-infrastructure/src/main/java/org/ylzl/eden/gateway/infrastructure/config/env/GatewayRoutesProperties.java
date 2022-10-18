package org.ylzl.eden.gateway.infrastructure.config.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网关路由配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = GatewayRoutesProperties.PREFIX)
public class GatewayRoutesProperties {

	public static final String PREFIX = "spring.cloud.gateway.routes";

	private final Nacos nacos = new Nacos();

	@Data
	public static class Nacos {

		private boolean enabled;

		private String dataId;
	}
}

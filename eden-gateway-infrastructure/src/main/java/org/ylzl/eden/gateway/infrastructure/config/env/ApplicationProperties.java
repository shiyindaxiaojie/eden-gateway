package org.ylzl.eden.gateway.infrastructure.config.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@Data
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

}

package org.ylzl.eden.gateway.infrastructure.config.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.gateway.infrastructure.config.env.ApplicationProperties;

/**
 * 应用自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@EnableConfigurationProperties({ApplicationProperties.class})
@Slf4j
@Configuration
public class ApplicationAutoConfiguration {
}

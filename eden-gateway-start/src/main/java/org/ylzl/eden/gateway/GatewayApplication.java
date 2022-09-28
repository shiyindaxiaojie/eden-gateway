package org.ylzl.eden.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.ylzl.eden.spring.framework.bootstrap.SpringBootApplicationTemplate;

/**
 * Spring Boot 引导类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EnableDiscoveryClient
@Slf4j
@SpringBootApplication
public class GatewayApplication extends SpringBootApplicationTemplate {

	/**
	 * 启动入口
	 *
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		run(GatewayApplication.class, args, WebApplicationType.REACTIVE);
	}
}

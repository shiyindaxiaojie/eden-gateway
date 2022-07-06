package org.ylzl.eden.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.ylzl.eden.spring.framework.bootstrap.SpringBootApplicationTemplate;

/**
 * Spring Boot 引导类
 *
 * @author gyl
 * @since 2.4.x
 */
@EnableDiscoveryClient
@Slf4j
@SpringBootApplication
public class Application extends SpringBootApplicationTemplate {

	/**
	 * 启动入口
	 *
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		run(Application.class, args, WebApplicationType.REACTIVE);
	}
}

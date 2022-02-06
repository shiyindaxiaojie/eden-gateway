package org.ylzl.eden.gateway.infrastructure.config.autoconfigure;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.gateway.infrastructure.config.env.GatewayRoutesRefreshProperties;
import org.ylzl.eden.gateway.infrastructure.config.event.GatewayRoutesRefreshedEventPublisher;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 网关路由刷新自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@EnableConfigurationProperties(GatewayRoutesRefreshProperties.class)
@Slf4j
@Configuration
public class GatewayRoutesRefreshAutoConfiguration {

	private final GatewayRoutesRefreshedEventPublisher publisher;

	private final GatewayRoutesRefreshProperties properties;

	private final NacosConfigProperties nacosConfigProperties;

	public GatewayRoutesRefreshAutoConfiguration(GatewayRoutesRefreshedEventPublisher publisher, GatewayRoutesRefreshProperties properties, NacosConfigProperties nacosConfigProperties) {
		this.publisher = publisher;
		this.properties = properties;
		this.nacosConfigProperties = nacosConfigProperties;
	}

	@PostConstruct
	public void init() throws NacosException {
		ConfigService configService = this.createConfigService();
		String config =
			configService.getConfig(properties.getNacosConfig().getDataId(),
				nacosConfigProperties.getGroup(),
				nacosConfigProperties.getTimeout());
		List<RouteDefinition> definitionList = JSONUtil.toList(config, RouteDefinition.class);
		if (CollectionUtils.isNotEmpty(definitionList)) {
			definitionList.forEach(publisher::add);
		}
		this.addConfigListener(configService);
	}

	private ConfigService createConfigService() throws NacosException {
		Properties properties = new Properties();
		properties.setProperty("serverAddr", nacosConfigProperties.getServerAddr());
		properties.setProperty("namespace", nacosConfigProperties.getNamespace());
		return NacosFactory.createConfigService(properties);
	}

	private void addConfigListener(ConfigService configService) throws NacosException {
		configService.addListener(properties.getNacosConfig().getDataId(),
			nacosConfigProperties.getGroup(), new Listener() {

				@Override
				public void receiveConfigInfo(String configInfo) {
					List<RouteDefinition> definitionList = JSONUtil.toList(configInfo, RouteDefinition.class);
					publisher.update(definitionList);
				}

				@Override
				public Executor getExecutor() {
					return null;
				}
			});
	}
}

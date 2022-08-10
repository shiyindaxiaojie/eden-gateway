package org.ylzl.eden.gateway.infrastructure.config.autoconfigure;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.gateway.infrastructure.config.env.GatewayRoutesRefreshProperties;
import org.ylzl.eden.gateway.infrastructure.config.event.GatewayRoutesRefreshedEventPublisher;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Nacos 刷新网关路由自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnExpression("${" + GatewayRoutesRefreshProperties.PREFIX + ".nacos.enabled:false}")
@EnableConfigurationProperties(GatewayRoutesRefreshProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration
public class NacosRefreshRoutesAutoConfiguration {

	private final GatewayRoutesRefreshedEventPublisher publisher;

	private final GatewayRoutesRefreshProperties gatewayProperties;

	private final NacosConfigManager nacosConfigManager;

	private final NacosConfigProperties nacosConfigProperties;

	@PostConstruct
	public void init() throws NacosException {
		ConfigService configService = nacosConfigManager.getConfigService();
		String config =
			configService.getConfig(gatewayProperties.getNacos().getDataId(),
				nacosConfigProperties.getGroup(),
				nacosConfigProperties.getTimeout());
		List<RouteDefinition> definitionList = JSONUtil.toList(config, RouteDefinition.class);
		if (CollectionUtils.isNotEmpty(definitionList)) {
			definitionList.forEach(publisher::add);
		}

		configService.addListener(gatewayProperties.getNacos().getDataId(),
				nacosConfigProperties.getGroup(),
				new AbstractListener() {

					@Override
					public void receiveConfigInfo(String configInfo) {
						List<RouteDefinition> definitionList = JSONUtil.toList(configInfo, RouteDefinition.class);
						publisher.update(definitionList);
					}
				});
	}
}

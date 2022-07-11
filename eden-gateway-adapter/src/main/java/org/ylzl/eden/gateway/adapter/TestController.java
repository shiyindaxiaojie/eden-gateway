package org.ylzl.eden.gateway.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ylzl.eden.demo.client.user.api.UserService;
import org.ylzl.eden.demo.client.user.dto.UserDTO;
import org.ylzl.eden.demo.client.user.dto.query.UserByIdQry;
import org.ylzl.eden.demo.client.user.dto.query.UserListByPageQry;
import org.ylzl.eden.gateway.adapter.constant.ApiConstant;
import org.ylzl.eden.spring.data.redis.core.CustomRedisTemplate;
import org.ylzl.eden.spring.framework.cola.dto.SingleResponse;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
@RequestMapping(ApiConstant.WEB_API_PATH + "/users")
@RestController
public class TestController {

    @DubboReference
	private UserService userService;

	private final CustomRedisTemplate customRedisTemplate;

	/**
	 * 根据主键获取用户信息
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public SingleResponse<UserDTO> getUserById(@PathVariable Long id) {
		customRedisTemplate.get("test");
			userService.listUserByPage(UserListByPageQry.builder().login(
				"admin").build());
		return userService.getUserById(UserByIdQry.builder().id(id).build());
	}
}

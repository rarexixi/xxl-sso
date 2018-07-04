package org.xi.sso.server.controller;

import org.xi.sso.core.model.ReturnVo;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.SsoLoginHelper;
import org.xi.sso.server.entity.UserEntity;
import org.xi.sso.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private UserService userService;

    /**
     * Login
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public ReturnVo<String> login(String username, String password) {

        if (StringUtils.isBlank(username)) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "请输入用户名");
        }
        if (StringUtils.isBlank(password)) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "请输入密码");
        }
        UserEntity user = userService.getByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "用户名或密码错误");
        }

        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(user.getId());
        ssoUser.setUsername(user.getUsername());

        String sessionId = UUID.randomUUID().toString();
        SsoLoginHelper.login(sessionId, ssoUser);

        return new ReturnVo<>(sessionId);
    }


    /**
     * Logout
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logout")
    public ReturnVo<String> logout(String sessionId) {
        SsoLoginHelper.logout(sessionId);
        return ReturnVo.SUCCESS;
    }

    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logincheck")
    public ReturnVo<SsoUser> loginCheck(String sessionId) {
        SsoUser ssoUser = SsoLoginHelper.loginCheck(sessionId);
        if (ssoUser == null) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "用户未登录");
        }
        return new ReturnVo<>(ssoUser);
    }

}
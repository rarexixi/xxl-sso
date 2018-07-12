package org.xi.sso.server.controller;

import org.xi.sso.core.model.ReturnVo;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.SsoLoginHelper;
import org.xi.sso.server.entity.UserEntity;
import org.xi.sso.server.model.LoginModel;
import org.xi.sso.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xi.sso.server.utils.SsoUserUtils;

import java.util.UUID;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private UserService userService;

    /**
     * app 登录接口
     *
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public ReturnVo<String> login(LoginModel model) {

        if (StringUtils.isBlank(model.getUsername())) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "请输入用户名");
        }
        if (StringUtils.isBlank(model.getPassword())) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "请输入密码");
        }
        UserEntity user = userService.getByUsername(model.getUsername());
        if (user == null || !user.getPassword().equals(model.getPassword())) {
            return new ReturnVo<>(ReturnVo.FAIL_CODE, "用户名或密码错误");
        }

        SsoUser ssoUser = SsoUserUtils.getSsoUser(user);

        String sessionId = UUID.randomUUID().toString();
        SsoLoginHelper.login(sessionId, ssoUser);

        return new ReturnVo<>(sessionId);
    }


    /**
     * app 注销接口
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
     * app 登录验证
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

    @RequestMapping("/throw")
    public String throwException() throws Exception {
        throw new Exception("hello exception");
    }
}
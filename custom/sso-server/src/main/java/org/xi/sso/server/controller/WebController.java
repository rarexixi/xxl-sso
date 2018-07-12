package org.xi.sso.server.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.SsoLoginHelper;
import org.xi.sso.core.util.UrlUtil;
import org.xi.sso.server.entity.UserEntity;
import org.xi.sso.server.model.LoginModel;
import org.xi.sso.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xi.sso.server.utils.SsoUserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * sso server (for web)
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private UserService userService;

    /**
     * web 登录
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

        SsoUser ssoUser = SsoLoginHelper.loginCheck(request);

        String redirectUrl = UrlUtil.decode(request.getParameter(SsoConf.REDIRECT_URL));

        // 用户为空，跳转到登录页
        if (ssoUser == null) {
            model.addAttribute("errorMsg", request.getParameter("errorMsg"));
            LoginModel loginModel = new LoginModel();
            loginModel.setRedirectUrl(redirectUrl);
            model.addAttribute("model", loginModel);
            return "web/login";
        }

        // 重定向为空，返回主页
        if (StringUtils.isBlank(redirectUrl)) {
            return "redirect:/";
        }

        // 返回重定向页面
        String sessionId = SsoLoginHelper.getSessionIdByCookie(request);
        Map<String, String> params = new HashMap<>();
        params.put(SsoConf.SSO_SESSION_ID, sessionId);
        return "redirect:" + UrlUtil.getUrl(redirectUrl, params);
    }

    /**
     * web 登录
     *
     * @param model
     * @param loginModel
     * @param errors
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @Valid @ModelAttribute("model") LoginModel loginModel, Errors errors,
                        HttpServletRequest request, HttpServletResponse response) {

        if (errors.hasErrors()) {
            return "web/login";
        }

        UserEntity user = userService.getByUsername(loginModel.getUsername());
        if (user == null || !user.getPassword().equals(loginModel.getPassword())) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            model.addAttribute("model", loginModel);
            return "web/login";
        }

        SsoUser ssoUser = SsoUserUtils.getSsoUser(user);

        String sessionId = UUID.randomUUID().toString();
        SsoLoginHelper.login(response, sessionId, ssoUser);

        if (StringUtils.isBlank(loginModel.getRedirectUrl())) {
            return "redirect:/";
        }
        Map<String, String> params = new HashMap<>();
        params.put(SsoConf.SSO_SESSION_ID, sessionId);
        return "redirect:" + UrlUtil.getUrl(loginModel.getRedirectUrl(), params);
    }

    /**
     * web 注销
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        SsoLoginHelper.logout(request, response);
        String redirectUrl = UrlUtil.decode(request.getParameter(SsoConf.REDIRECT_URL));
        if (StringUtils.isBlank(redirectUrl)) {
            return "redirect:/web/login";
        }
        return "redirect:" + redirectUrl;
    }

}
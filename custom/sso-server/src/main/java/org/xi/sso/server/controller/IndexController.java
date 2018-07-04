package org.xi.sso.server.controller;

import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.SsoLoginHelper;
import org.xi.sso.server.entity.UserEntity;
import org.xi.sso.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * sso server (for web)
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {

        SsoUser ssoUser = SsoLoginHelper.loginCheck(request);
        if (ssoUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("ssoUser", ssoUser);
        return "index";
    }

    /**
     * Login page
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(SsoConf.SSO_LOGIN)
    public String login(Model model, HttpServletRequest request) {

        SsoUser ssoUser = SsoLoginHelper.loginCheck(request);

        // 用户为空，跳转到登录页
        if (ssoUser == null) {
            model.addAttribute("errorMsg", request.getParameter("errorMsg"));
            model.addAttribute(SsoConf.REDIRECT_URL, request.getParameter(SsoConf.REDIRECT_URL));
            return "login";
        }

        // 重定向为空，返回主页
        String redirectUrl = request.getParameter(SsoConf.REDIRECT_URL);
        if (StringUtils.isBlank(redirectUrl)) {
            return "redirect:/";
        }

        // 返回重定向页面
        String sessionId = SsoLoginHelper.getSessionIdByCookie(request);
        String redirectUrlFinal = redirectUrl + "?" + SsoConf.SSO_SESSION_ID + "=" + sessionId;
        return "redirect:" + redirectUrlFinal;
    }

    /**
     * Login
     *
     * @param request
     * @param redirectAttributes
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,
                          String username, String password) {

        String errorMsg = null;
        if (StringUtils.isBlank(username)) errorMsg = "请输入用户名";
        if (StringUtils.isBlank(password)) errorMsg = "请输入密码";

        UserEntity user = userService.getByUsername(username);
        if (user == null || !user.getPassword().equals(password)) errorMsg = "用户名或密码错误";

        String redirectUrl = request.getParameter(SsoConf.REDIRECT_URL);

        if (errorMsg != null) {
            redirectAttributes.addAttribute("errorMsg", errorMsg);
            redirectAttributes.addAttribute(SsoConf.REDIRECT_URL, redirectUrl);
            return "redirect:/login";
        }

        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(user.getId());
        ssoUser.setUsername(user.getUsername());

        String sessionId = UUID.randomUUID().toString();
        SsoLoginHelper.login(response, sessionId, ssoUser);

        if (StringUtils.isBlank(redirectUrl)) {
            return "redirect:/";
        }
        String redirectUrlFinal = redirectUrl + "?" + SsoConf.SSO_SESSION_ID + "=" + sessionId;
        return "redirect:" + redirectUrlFinal;
    }

    /**
     * Logout
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(SsoConf.SSO_LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        SsoLoginHelper.logout(request, response);
        redirectAttributes.addAttribute(SsoConf.REDIRECT_URL, request.getParameter(SsoConf.REDIRECT_URL));
        return "redirect:/login";
    }

}
package org.xi.sso.server.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.SsoLoginHelper;
import org.xi.sso.server.entity.UserEntity;
import org.xi.sso.server.model.LoginModel;
import org.xi.sso.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {

        SsoUser ssoUser = SsoLoginHelper.loginCheck(request);

        // 用户为空，跳转到登录页
        if (ssoUser == null) {
            model.addAttribute("errorMsg", request.getParameter("errorMsg"));
            model.addAttribute("model", new LoginModel());
            model.addAttribute(SsoConf.REDIRECT_URL, request.getParameter(SsoConf.REDIRECT_URL));
            return "web/login";
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
     * web 登录
     *
     * @param request
     * @param response
     * @param model
     * @param loginModel
     * @param errors
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model,
                        @Valid @ModelAttribute("model") LoginModel loginModel, Errors errors) {

        if (errors.hasErrors()) {
            return "web/login";
        }

        String redirectUrl = request.getParameter(SsoConf.REDIRECT_URL);

        UserEntity user = userService.getByUsername(loginModel.getUsername());
        if (user == null || !user.getPassword().equals(loginModel.getPassword())) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            model.addAttribute("model", loginModel);
            model.addAttribute(SsoConf.REDIRECT_URL, redirectUrl);
            return "web/login";
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
     * web 注销
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        SsoLoginHelper.logout(request, response);
        redirectAttributes.addAttribute(SsoConf.REDIRECT_URL, request.getParameter(SsoConf.REDIRECT_URL));
        return "redirect:/web/login";
    }

}
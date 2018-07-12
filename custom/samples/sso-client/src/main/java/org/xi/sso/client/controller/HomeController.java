package org.xi.sso.client.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.ReturnVo;
import org.xi.sso.core.model.SsoUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {

        SsoUser ssoUser = (SsoUser) request.getAttribute(SsoConf.SSO_USER);
        model.addAttribute("ssoUser", ssoUser);
        return "index";
    }

    @RequestMapping("/getuser")
    @ResponseBody
    public ReturnVo<SsoUser> getUser(HttpServletRequest request) {
        SsoUser user = (SsoUser) request.getAttribute(SsoConf.SSO_USER);
        return new ReturnVo<>(user);
    }

    @RequestMapping("/relay")
    public String relay(String redirectUrl) {
        if (StringUtils.isBlank(redirectUrl)) {
            return "redirect:/";
        }
        return "redirect:" + redirectUrl;
    }

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(@RequestParam(defaultValue = "", required = false) String name) {
        return "hello " + name;
    }
}

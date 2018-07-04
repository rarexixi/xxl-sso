package org.xi.sso.client.controller;

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

    @RequestMapping("/json")
    @ResponseBody
    public ReturnVo<SsoUser> json(HttpServletRequest request) {
        SsoUser user = (SsoUser) request.getAttribute(SsoConf.SSO_USER);
        return new ReturnVo<>(user);
    }
}

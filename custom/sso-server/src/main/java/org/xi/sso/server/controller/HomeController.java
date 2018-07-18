package org.xi.sso.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {

        SsoUser ssoUser = (SsoUser) request.getAttribute(SsoConf.SSO_USER);
        model.addAttribute("ssoUser", ssoUser);
        return "index";
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public SsoUser getUser(HttpServletRequest request) {

        SsoUser ssoUser = (SsoUser) request.getAttribute(SsoConf.SSO_USER);
        return ssoUser;
    }
}

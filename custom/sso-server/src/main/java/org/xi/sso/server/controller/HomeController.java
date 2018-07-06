package org.xi.sso.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.SsoLoginHelper;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {

        SsoUser ssoUser = SsoLoginHelper.loginCheck(request);
        if (ssoUser == null) {
            return "redirect:/web/login";
        }
        model.addAttribute("ssoUser", ssoUser);
        return "index";
    }
}

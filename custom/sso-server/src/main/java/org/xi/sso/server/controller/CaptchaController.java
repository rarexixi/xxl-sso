package org.xi.sso.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xi.sso.server.service.CaptchaService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    CaptchaService captchaService;

    @RequestMapping("/get")
    public void getCaptchaImage(HttpServletResponse response,
                                @RequestParam(value = "width", defaultValue = "150") Integer width,
                                @RequestParam(value = "height", defaultValue = "40") Integer height) {

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        captchaService.outputImage(response, width, height);
    }
}

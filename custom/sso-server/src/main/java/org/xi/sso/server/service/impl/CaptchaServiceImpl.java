package org.xi.sso.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xi.sso.core.util.CaptchaUtils;
import org.xi.sso.core.util.CookieUtil;
import org.xi.sso.core.util.JedisUtil;
import org.xi.sso.server.properties.CaptchaProperties;
import org.xi.sso.server.service.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    CaptchaProperties captchaProperties;

    @Override
    public void outputImage(HttpServletResponse response, int width, int height) {

        String code = CaptchaUtils.generateCaptcha(captchaProperties.getCodeLength());
        String captchaId = UUID.randomUUID().toString();
        CookieUtil.set(response, captchaProperties.getCookieId(), captchaId, captchaProperties.getCookieMaxTime());
        JedisUtil.setStringValue(captchaId, code, captchaProperties.getRedisMaxTime());
        try {
            CaptchaUtils.outputImage(width, height, response.getOutputStream(), code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean test(HttpServletRequest request, String code) {

        String captchaId = CookieUtil.getValue(request, captchaProperties.getCookieId());
        String captcha = JedisUtil.getStringValue(captchaId);
        JedisUtil.del(captchaId);

        return captcha.equalsIgnoreCase(captcha);
    }
}

package org.xi.sso.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CaptchaService {

    void outputImage(HttpServletResponse response, int width, int height);
    boolean test(HttpServletRequest request, String code);
}

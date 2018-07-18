package org.xi.sso.server.interceptor;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.JacksonUtil;
import org.xi.sso.core.util.SsoLoginHelper;
import org.xi.sso.core.util.UrlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod method = (HandlerMethod) handler;

        String link = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            link += "?" + request.getQueryString();
        }

        SsoUser ssoUser = SsoLoginHelper.loginCheck(request);
        if (ssoUser == null) {
            ResponseBody responseBody;
            RestController restController;
            if ((responseBody = method.getMethodAnnotation(ResponseBody.class)) != null
                    || (restController = method.getBeanType().getAnnotation(RestController.class)) != null) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().println(JacksonUtil.toJson(SsoConf.SSO_LOGIN_FAIL_RESULT));
            } else {
                Map<String, String> params = new HashMap<>();
                params.put(SsoConf.REDIRECT_URL, UrlUtil.encode(link));

                String loginPageUrl = UrlUtil.getUrl(request.getContextPath() + "/web/login", params);
                response.sendRedirect(loginPageUrl);
            }
            return false;
        }

        request.setAttribute(SsoConf.SSO_USER, ssoUser);
        return super.preHandle(request, response, handler);
    }

}

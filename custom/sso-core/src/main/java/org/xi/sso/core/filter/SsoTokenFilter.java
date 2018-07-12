package org.xi.sso.core.filter;

import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.JacksonUtil;
import org.xi.sso.core.util.SsoLoginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoTokenFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SsoTokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String sessionId = SsoLoginHelper.cookieSessionIdGetByHeader(req);
        SsoUser ssoUser = SsoLoginHelper.loginCheck(sessionId);

        if (ssoUser == null) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println(JacksonUtil.toJson(SsoConf.SSO_LOGIN_FAIL_RESULT));
            return;
        }

        request.setAttribute(SsoConf.SSO_USER, ssoUser);
        chain.doFilter(request, response);
        return;
    }

}

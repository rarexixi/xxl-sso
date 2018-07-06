package org.xi.sso.core.filter;

import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.ReturnVo;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.JacksonUtil;
import org.xi.sso.core.util.SsoLoginHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoTokenFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SsoTokenFilter.class);

    private String ssoServer;
    private String loginPath;
    private String logoutPath;

    public SsoTokenFilter(String ssoServer, String loginPath, String logoutPath) {
        this.ssoServer = ssoServer;
        this.loginPath = loginPath;
        this.logoutPath = logoutPath;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String servletPath = ((HttpServletRequest) request).getServletPath();
        String sessionId = SsoLoginHelper.cookieSessionIdGetByHeader(req);
        SsoUser ssoUser = SsoLoginHelper.loginCheck(sessionId);

        if (StringUtils.isNotBlank(logoutPath) && logoutPath.equals(servletPath)) {
            if (ssoUser != null) {
                SsoLoginHelper.logout(sessionId);
            }
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println(JacksonUtil.toJson(new ReturnVo(ReturnVo.SUCCESS_CODE, null)));
            return;
        }

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

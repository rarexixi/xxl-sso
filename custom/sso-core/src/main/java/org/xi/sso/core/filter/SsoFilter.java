package org.xi.sso.core.filter;

import org.xi.sso.core.conf.SsoConf;
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

public class SsoFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SsoFilter.class);

    private String ssoServer;
    private String loginPath;
    private String logoutPath;

    public SsoFilter(String ssoServer, String loginPath, String logoutPath) {
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
        String link = req.getRequestURL().toString();

        if (StringUtils.isNotBlank(logoutPath) && logoutPath.equals(servletPath)) {
            SsoLoginHelper.removeSessionIdInCookie(req, res);
            String logoutPageUrl = ssoServer.concat(logoutPath);
            res.sendRedirect(logoutPageUrl);
            return;
        }

        String cookieSessionId = SsoLoginHelper.getSessionIdByCookie(req);
        SsoUser ssoUser = SsoLoginHelper.loginCheck(cookieSessionId);

        if (ssoUser == null) {
            SsoLoginHelper.removeSessionIdInCookie(req, res);
            String paramSessionId = request.getParameter(SsoConf.SSO_SESSION_ID);
            if (paramSessionId != null) {
                ssoUser = SsoLoginHelper.loginCheck(paramSessionId);
                if (ssoUser != null) {
                    SsoLoginHelper.setSessionIdInCookie(res, paramSessionId);
                }
            }
        }

        if (ssoUser == null) {
            String header = req.getHeader("content-type");
            boolean isJson = header != null && header.contains("json");
            if (isJson) {
                res.setContentType("application/json;charset=utf-8");
                res.getWriter().println(JacksonUtil.toJson(SsoConf.SSO_LOGIN_FAIL_RESULT));
                return;
            } else {
                String loginPageUrl = ssoServer.concat(loginPath) + "?" + SsoConf.REDIRECT_URL + "=" + link;
                res.sendRedirect(loginPageUrl);
                return;
            }
        }

        request.setAttribute(SsoConf.SSO_USER, ssoUser);
        chain.doFilter(request, response);
        return;
    }

}

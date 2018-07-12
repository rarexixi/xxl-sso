package org.xi.sso.core.filter;

import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.JacksonUtil;
import org.xi.sso.core.util.SsoLoginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.sso.core.util.UrlUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SsoFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SsoFilter.class);

    private String ssoServer;
    private String loginPath;
    private Set<String> excludePaths;

    public SsoFilter(String ssoServer, String loginPath) {
        this(ssoServer, loginPath, null);
    }

    public SsoFilter(String ssoServer, String loginPath, Set<String> excludePaths) {
        this.ssoServer = ssoServer;
        this.loginPath = loginPath;
        this.excludePaths = excludePaths == null ? new HashSet<>() : excludePaths;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String servletPath = req.getServletPath();
        if (excludePaths.contains(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        String link = req.getRequestURL().toString();
        if (req.getQueryString() != null) {
            link += "?" + req.getQueryString();
        }

        String cookieSessionId = SsoLoginHelper.getSessionIdByCookie(req);
        SsoUser ssoUser = SsoLoginHelper.loginCheck(cookieSessionId);

        if (ssoUser == null) {
            SsoLoginHelper.removeSessionIdInCookie(req, res);
            // 第一次从单点登录服务器返回
            String paramSessionId = request.getParameter(SsoConf.SSO_SESSION_ID);
            if (paramSessionId != null) {
                ssoUser = SsoLoginHelper.loginCheck(paramSessionId);
                if (ssoUser != null) {
                    SsoLoginHelper.setSessionIdInCookie(res, paramSessionId);
                }
            }
        }

        if (ssoUser == null) {

            String contentType, accept;
            if (((contentType = req.getHeader("content-type")) != null && contentType.contains("json"))
                    || ((accept = req.getHeader("accept")) != null && accept.contains("json"))) {
                // 如果请求是json
                res.setContentType("application/json;charset=utf-8");
                res.getWriter().println(JacksonUtil.toJson(SsoConf.SSO_LOGIN_FAIL_RESULT));
                return;
            } else {
                Map<String, String> params = new HashMap<>();
                params.put(SsoConf.REDIRECT_URL, UrlUtil.encode(link));
                String loginPageUrl = UrlUtil.getUrl(ssoServer.concat(loginPath), params);
                res.sendRedirect(loginPageUrl);
                return;
            }
        }

        request.setAttribute(SsoConf.SSO_USER, ssoUser);
        chain.doFilter(request, response);
        return;
    }

}

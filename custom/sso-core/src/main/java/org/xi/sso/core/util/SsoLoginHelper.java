package org.xi.sso.core.util;

import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.store.SsoLoginStore;
import org.xi.sso.core.model.SsoUser;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SsoLoginHelper {


    /**
     * get sessionId by cookie (web)
     *
     * @param request
     * @return
     */
    public static String getSessionIdByCookie(HttpServletRequest request) {
        String cookieSessionId = CookieUtil.getValue(request, SsoConf.SSO_SESSION_ID);
        return cookieSessionId;
    }

    /**
     * set sessionId in cookie (web)
     *
     * @param response
     * @param sessionId
     */
    public static void setSessionIdInCookie(HttpServletResponse response, String sessionId) {

        if (StringUtils.isNotBlank(sessionId)) {
            CookieUtil.set(response, SsoConf.SSO_SESSION_ID, sessionId, false);
        }
    }

    /**
     * remove sessionId in cookie (web)
     *
     * @param request
     * @param response
     */
    public static void removeSessionIdInCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, SsoConf.SSO_SESSION_ID);
    }

    /**
     * load cookie sessionId (app)
     *
     * @param request
     * @return
     */
    public static String cookieSessionIdGetByHeader(HttpServletRequest request) {
        String cookieSessionId = request.getHeader(SsoConf.SSO_SESSION_ID);
        return cookieSessionId;
    }

    /**
     * login check
     *
     * @param request
     * @return
     */
    public static SsoUser loginCheck(HttpServletRequest request) {
        String cookieSessionId = getSessionIdByCookie(request);
        if (StringUtils.isBlank(cookieSessionId)) return null;
        return loginCheck(cookieSessionId);
    }

    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static SsoUser loginCheck(String sessionId) {
        if (StringUtils.isBlank(sessionId)) return null;
        SsoUser ssoUser = SsoLoginStore.get(sessionId);
        return ssoUser;
    }

    /**
     * client login (web)
     *
     * @param response
     * @param sessionId
     * @param ssoUser
     */
    public static void login(HttpServletResponse response, String sessionId, SsoUser ssoUser) {

        SsoLoginStore.put(sessionId, ssoUser);
        CookieUtil.set(response, SsoConf.SSO_SESSION_ID, sessionId, false);
    }

    /**
     * client login (app)
     *
     * @param sessionId
     * @param ssoUser
     */
    public static void login(String sessionId, SsoUser ssoUser) {
        SsoLoginStore.put(sessionId, ssoUser);
    }


    /**
     * client logout (web)
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        String cookieSessionId = getSessionIdByCookie(request);

        if (StringUtils.isNotBlank(cookieSessionId)) {
            SsoLoginStore.remove(cookieSessionId);
        }
        CookieUtil.remove(request, response, SsoConf.SSO_SESSION_ID);
    }

    /**
     * client logout (app)
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {
        SsoLoginStore.remove(sessionId);
    }
}

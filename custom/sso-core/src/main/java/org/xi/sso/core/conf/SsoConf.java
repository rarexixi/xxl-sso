package org.xi.sso.core.conf;

import org.xi.sso.core.model.ReturnVo;

public class SsoConf {

    public static final String REDIRECT_URL = "redirect_url";
    public static final String SSO_SESSION_ID = "sso_session_id";
    public static final String SSO_USER = "sso_user";
    public static final String SSO_LOGIN = "/login";
    public static final String SSO_LOGOUT = "/logout";
    public static final ReturnVo<String> SSO_LOGIN_FAIL_RESULT = new ReturnVo(501, "sso not login.");
}

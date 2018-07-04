package org.xi.sso.core.store;

import org.xi.sso.core.conf.SsoConf;
import org.xi.sso.core.model.SsoUser;
import org.xi.sso.core.util.JedisUtil;

public class SsoLoginStore {

    public static SsoUser get(String sessionId) {

        String redisKey = redisKey(sessionId);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            SsoUser ssoUser = (SsoUser) objectValue;
            return ssoUser;
        }
        return null;
    }

    public static void remove(String sessionId) {
        String redisKey = redisKey(sessionId);
        JedisUtil.del(redisKey);
    }

    public static void put(String sessionId, SsoUser ssoUser) {
        String redisKey = redisKey(sessionId);
        JedisUtil.setObjectValue(redisKey, ssoUser);
    }

    private static String redisKey(String sessionId) {
        return SsoConf.SSO_SESSION_ID.concat("#").concat(sessionId);
    }

}

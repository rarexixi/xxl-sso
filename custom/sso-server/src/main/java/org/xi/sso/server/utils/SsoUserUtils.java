package org.xi.sso.server.utils;

import org.xi.sso.core.model.SsoUser;
import org.xi.sso.server.entity.UserEntity;

public class SsoUserUtils {

    public static SsoUser getSsoUser(UserEntity user) {
        if (user == null) return null;
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(user.getId());
        ssoUser.setUsername(user.getUsername());
        return ssoUser;
    }
}

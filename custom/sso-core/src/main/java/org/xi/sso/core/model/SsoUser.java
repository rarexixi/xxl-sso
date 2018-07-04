package org.xi.sso.core.model;

import java.io.Serializable;

public class SsoUser implements Serializable {

    private static final long serialVersionUID = 42L;

    private int userId;
    private String username;

    // TODO，custom

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

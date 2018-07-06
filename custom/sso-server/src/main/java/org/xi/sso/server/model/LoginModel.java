package org.xi.sso.server.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LoginModel {

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^.{4,20}$", message = "用户名必须是4-20位字符")
    private String username;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^.{6,}$", message = "密码不能小于6位")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

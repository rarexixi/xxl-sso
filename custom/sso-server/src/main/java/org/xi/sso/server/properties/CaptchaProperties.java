package org.xi.sso.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    private String cookieId = "captcha_id";
    private int codeLength = 6;
    private int cookieMaxTime = 60;
    private int redisMaxTime = 60;

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public int getCookieMaxTime() {
        return cookieMaxTime;
    }

    public void setCookieMaxTime(int cookieMaxTime) {
        this.cookieMaxTime = cookieMaxTime;
    }

    public int getRedisMaxTime() {
        return redisMaxTime;
    }

    public void setRedisMaxTime(int redisMaxTime) {
        this.redisMaxTime = redisMaxTime;
    }
}

package org.xi.sso.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "sso")
public class SsoProperties {

    private String server;
    private String loginPath;
    private String redisAddress;
    private String[] urlPatterns = {"/*"};
    private Set<String> excludePaths;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getRedisAddress() {
        return redisAddress;
    }

    public void setRedisAddress(String redisAddress) {
        this.redisAddress = redisAddress;
    }

    public String[] getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(String[] urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public Set<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(Set<String> excludePaths) {
        this.excludePaths = excludePaths;
    }
}

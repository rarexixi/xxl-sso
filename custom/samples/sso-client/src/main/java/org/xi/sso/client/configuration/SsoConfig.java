package org.xi.sso.client.configuration;

import org.xi.sso.core.filter.SsoFilter;
import org.xi.sso.core.util.JedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SsoConfig implements InitializingBean {

    @Value("${sso.server}")
    private String ssoServer;

    @Value("${sso.logout.path}")
    private String ssoLogoutPath;

    @Value("${sso.redis.address}")
    private String ssoRedisAddress;

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtil.init(ssoRedisAddress);
    }

    @Bean
    public FilterRegistrationBean ssoFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("SsoFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(new SsoFilter(ssoServer, ssoLogoutPath));
        return registration;
    }
}

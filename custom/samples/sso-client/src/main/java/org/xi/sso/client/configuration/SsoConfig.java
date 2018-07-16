package org.xi.sso.client.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.xi.sso.client.properties.SsoProperties;
import org.xi.sso.core.filter.SsoFilter;
import org.xi.sso.core.util.JedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SsoConfig implements InitializingBean {

    @Autowired
    SsoProperties ssoProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtil.init(ssoProperties.getRedisAddress());
    }

    @Bean
    public FilterRegistrationBean ssoFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("SsoFilter");
        registration.setOrder(2);
        registration.addUrlPatterns(ssoProperties.getUrlPatterns());
        registration.setFilter(new SsoFilter(ssoProperties.getServer(), ssoProperties.getLoginPath(), ssoProperties.getExcludePaths()));
        return registration;
    }
}

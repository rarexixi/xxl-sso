package org.xi.sso.client.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xi.sso.client.filter.CorsFilter;
import org.xi.sso.client.properties.CorsProperties;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    CorsProperties corsProperties;

    @Autowired
    CorsFilter corsFilter;

    @Bean
    public FilterRegistrationBean corsFilterRegistration() {

        CorsProperties.CorsFilterProperty filterProperty = corsProperties.getFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("corsFilter");
        registration.setOrder(1);
        registration.addUrlPatterns(filterProperty.getPathPattern());
        registration.setFilter(corsFilter);
        return registration;
    }

    /*
    // 单独设置跨域可用，但是没有找到在sso filter之前执行的方法
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        CorsRegistrationProperty[] corsRegistrations = corsProperties.getCorsRegistrations();

        if (corsRegistrations == null || corsRegistrations.length == 0) return;
        for (CorsRegistrationProperty property : corsRegistrations) {
            String pathPattern = property.getPathPattern();
            if (pathPattern == null || pathPattern.isEmpty()) continue;
            CorsRegistration corsRegistration = registry.addMapping(pathPattern);

            if (property.getAllowedOrigins() != null)
                corsRegistration.allowedOrigins(property.getAllowedOrigins());
            if (property.getAllowedMethods() != null)
                corsRegistration.allowedMethods(property.getAllowedMethods());
            if (property.getAllowedHeaders() != null)
                corsRegistration.allowedHeaders(property.getAllowedHeaders());
            if (property.getExposedHeaders() != null)
                corsRegistration.exposedHeaders(property.getExposedHeaders());
            if (property.getAllowCredentials() != null)
                corsRegistration.allowCredentials(property.getAllowCredentials());
            if (property.getMaxAge() != null)
                corsRegistration.maxAge(property.getMaxAge());
        }
    }
    */

}

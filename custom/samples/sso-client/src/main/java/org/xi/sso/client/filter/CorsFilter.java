package org.xi.sso.client.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xi.sso.client.properties.CorsProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Autowired
    CorsProperties corsProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse rep = (HttpServletResponse) servletResponse;

        CorsProperties.CorsFilterProperty filterProperty = corsProperties.getFilter();

        // 前段跨域请求带Cookie时只能设置一个，并且是页面的域名
        if (filterProperty.getAllowedOrigins() != null)
            rep.setHeader("Access-Control-Allow-Origin", filterProperty.getAllowedOrigins());
        // 前段跨域请求带Cookie时必须设置为true
        if (filterProperty.getAllowCredentials() != null)
            rep.setHeader("Access-Control-Allow-Credentials", filterProperty.getAllowCredentials().toString());
        if (filterProperty.getAllowedMethods() != null)
            rep.setHeader("Access-Control-Allow-Methods", filterProperty.getAllowedMethods());
        if (filterProperty.getAllowedHeaders() != null)
            rep.setHeader("Access-Control-Allow-Headers", filterProperty.getAllowedHeaders());
        if (filterProperty.getMaxAge() != null)
            rep.setHeader("Access-Control-Max-Age", filterProperty.getMaxAge().toString());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

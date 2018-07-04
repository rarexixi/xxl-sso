package org.xi.sso.server.configuration;

import org.xi.sso.core.util.JedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SsoRedisConfig implements InitializingBean {

    @Value("${redis.address}")
    private String redisAddress;

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtil.init(redisAddress);
    }
}

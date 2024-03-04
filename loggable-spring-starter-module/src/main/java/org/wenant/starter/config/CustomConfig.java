package org.wenant.starter.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wenant.starter.aspect.LoggableAspect;

@Configuration
public class CustomConfig {
    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }


}

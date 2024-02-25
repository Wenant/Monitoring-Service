package org.wenant.starter.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wenant.starter.aspect.AuditAspect;

@Configuration
public class CustomConfig {

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}

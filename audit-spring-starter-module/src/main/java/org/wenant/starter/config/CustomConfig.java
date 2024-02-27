package org.wenant.starter.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wenant.starter.aspect.AuditAspect;
import org.wenant.starter.domain.repository.AuditRepository;
import org.wenant.starter.service.JwtServiceInterface;

@Configuration
public class CustomConfig {

    private final AuditRepository auditRepository;
    private final JwtServiceInterface jwtService;

    @Autowired
    public CustomConfig(AuditRepository auditRepository, JwtServiceInterface jwtService) {
        this.auditRepository = auditRepository;
        this.jwtService = jwtService;
    }

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect(auditRepository, jwtService);
    }
}

package org.wenant.starter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wenant.starter.domain.audit.MeterReadingData;
import org.wenant.starter.domain.audit.MeterTypeData;
import org.wenant.starter.domain.audit.UserData;
import org.wenant.starter.domain.entity.Audit;
import org.wenant.starter.domain.repository.AuditRepository;
import org.wenant.starter.service.JwtServiceInterface;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Aspect
@Component
public class AuditAspect {
    private final AuditRepository auditRepository;
    private final JwtServiceInterface jwtService;

    @Autowired
    public AuditAspect(AuditRepository auditRepository, JwtServiceInterface jwtService) {
        this.auditRepository = auditRepository;
        this.jwtService = jwtService;
    }

    @Pointcut("within(@org.wenant.starter.annotations.EnableAudit *) && execution(* * (..))")
    public void annotatedByEnableAudit() {
    }

    @Around("annotatedByEnableAudit()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        try {
            switch (methodName) {
                case "addUser":
                    auditAddUser(args);
                    break;
                case "addMeterReading":
                    auditAddMeterReading(args);
                    break;
                case "addMeterType":
                    auditAddMeterType(args);
                    break;


            }
        } catch (Exception e) {
            System.out.println("Audit aspect error for " + methodName + ": " + e.getMessage());
        }

        return result;
    }

    private void createAndSaveAudit(String action, String tableName, Long userId, String newValue) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        Audit audit = new Audit(null, userId, action, timestamp, tableName, newValue);
        auditRepository.save(audit);
    }

    private void auditAddUser(Object[] args) {
        UserData userData = (UserData) args[0];
        Long userId = auditRepository.getUserIdByUsername(userData.getUsername());
        createAndSaveAudit("Insert", "users", userId, userData.getUsername());
    }

    private void auditAddMeterReading(Object[] args) {
        MeterReadingData readings = (MeterReadingData) args[0];
        createAndSaveAudit("Insert", "meter_readings", readings.getUserId(),
                readings.getMeterType() + ": " + readings.getValue());
    }

    private void auditAddMeterType(Object[] args) {
        MeterTypeData meterType = (MeterTypeData) args[0];
        String header = (String) args[1];
        String username = jwtService.getUsernameFromAuthorizationHeader(header);
        Long userId = auditRepository.getUserIdByUsername(username);
        createAndSaveAudit("Insert", "meter_type_catalog", userId, meterType.getMeterType());
    }
}
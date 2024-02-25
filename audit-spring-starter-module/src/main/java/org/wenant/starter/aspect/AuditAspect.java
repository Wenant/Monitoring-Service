package org.wenant.starter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wenant.starter.domain.audit.MeterReadingData;
import org.wenant.starter.domain.audit.UserData;
import org.wenant.starter.domain.entity.Audit;
import org.wenant.starter.domain.repository.JdbcAuditRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Aspect
@Component
public class AuditAspect {
    @Autowired
    private JdbcAuditRepository auditRepository;

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
                    UserData userData = (UserData) args[0];
                    Long userId = auditRepository.getUserIdByUsername(userData.getUsername());
                    createAndSaveAudit("Insert", "users", userId, userData.getUsername());
                    break;
                case "addMeterReading":
                    MeterReadingData readings = (MeterReadingData) args[0];
                    createAndSaveAudit("Insert", "meter_readings", readings.getUserId(),
                            readings.getMeterType() + ": " + readings.getValue());
                    break;
                // TODO: for new meter type's

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
}
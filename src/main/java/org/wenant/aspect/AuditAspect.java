package org.wenant.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wenant.service.in.JwtService;


@Aspect
@Component
public class AuditAspect {
    private static final Logger logger = LoggerFactory.getLogger("audit");

    private final JwtService jwtService;

    @Autowired
    public AuditAspect(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Pointcut("within(@org.springframework.stereotype.Controller *) && execution(* * (..))")
    public void controllerMethods() {
    }

    @AfterReturning(pointcut = "execution(* org.wenant.controller.*.*(..))", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        String substringResult = (authorizationHeader != null) ? authorizationHeader.substring(7) : null;

        String requestMethod = request.getMethod();
        String methodName = joinPoint.getSignature().getName();
        String requestURI = request.getRequestURI();
        String username;
        if (substringResult != null) {
            String token = authorizationHeader.substring(7);
            username = jwtService.getUsernameFromToken(token);
        } else {
            username = "без авторизации";
        }

        logger.info("Пользователь: " + username
                + " вызывает метод: " + methodName
                + " на  эндпойнте: " + requestMethod + ":" + requestURI);
    }

}
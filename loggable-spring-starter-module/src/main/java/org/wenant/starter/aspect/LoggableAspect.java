package org.wenant.starter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggableAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggableAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Controller *) && execution(* * (..))")
    public void controllerMethods() {
    }

    @Around("execution(* org.wenant.controller.*.*(..))")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        logger.info("Method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + end + " ms.");
        return result;
    }

}
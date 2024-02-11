package org.wenant.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wenant.service.in.JwtService;

@Aspect
public class AuditAspect {
    private static final Logger logger = LoggerFactory.getLogger("audit");
    private final JwtService jwtService = new JwtService();

    @Pointcut("within(@org.wenant.annotations.Audit *) && execution(* * (..))")
    public void annotatedByAudit() {
    }

    @Around("annotatedByAudit()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        Object[] args = proceedingJoinPoint.getArgs();
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }

        String methodName = test(request);

        String authorizationKey = null;
        String username = null;
        if (request != null) {
            authorizationKey = request.getHeader("Authorization");
            if (authorizationKey != null && authorizationKey.startsWith("Bearer ")) {
                String token = authorizationKey.substring(7);
                username = jwtService.getUsernameFromToken(token);
            }
        }

        logger.info("Пользователь:" + username + " " + methodName);
        return result;
    }

    private String test(HttpServletRequest request) {
        String out = null;
        String pathInfo = request.getServletPath();
        String path2 = request.getPathInfo();
        if (path2 != null) {
            pathInfo += path2;
        }

        if (pathInfo != null) {
            String[] pathSegments = pathInfo.split("/");

            if ("login".equals(pathSegments[1])) {
                out = "запрос токена авторизации";

            } else if ("register".equals(pathSegments[1])) {
                out = "регистрация";

            } else if ("meterTypes".equals(pathSegments[1])) {
                out = "получение списка типов счетчиков";

            } else if ("readings".equals(pathSegments[1])) {
                if (pathSegments.length == 2) {
                    out = "добавляет новые показания счетчиков";
                } else if ("history".equals(pathSegments[2])) {
                    out = "просмотр истории подачи показаний";
                } else if ("latest".equals(pathSegments[2])) {
                    out = "просмотр актуальных показаний";
                } else if (pathSegments.length == 4) {
                    out = "просмотр показаний за " + pathSegments[2] + "/" + pathSegments[3];
                }

            } else if (pathSegments.length == 4 && "admin".equals(pathSegments[1]) && "types".equals(pathSegments[3])) {
                out = "добавил новый тип счетчиков";
            } else if ("admin".equals(pathSegments[1])) {
                if ("users".equals(pathSegments[2])) {
                    out = "посмотрел список пользователей";
                } else if ("history".equals(pathSegments[4])) {
                    out = "посмотрел истории подачи показаний пользователя: " + pathSegments[2];
                } else if ("latest".equals(pathSegments[4])) {
                    out = "посмотрел актуальные показания пользователя: " + pathSegments[2];
                } else if (pathSegments.length == 6) {
                    out = "просмотр показаний пользователя" + pathSegments[2] +
                            " за " + pathSegments[4] + "/" + pathSegments[5];
                }

            }

        }
        if (out == null) {
            out = "неизвестный запрос";
        }

        return out;

    }
}
package com.re.it211_project.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.re.it211_project.service..*(..))")
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long startTime = System.currentTimeMillis();

        try {

            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();

            log.info(
                    "[SUCCESS] {} executed in {} ms",
                    joinPoint.getSignature().toShortString(),
                    (endTime - startTime)
            );

            return result;

        } catch (Exception e) {

            long endTime = System.currentTimeMillis();

            log.error(
                    "[ERROR] {} failed after {} ms : {}",
                    joinPoint.getSignature().toShortString(),
                    (endTime - startTime),
                    e.getMessage()
            );

            throw e;
        }
    }
}
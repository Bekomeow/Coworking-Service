package org.beko.aspects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

/**
 * Aspect for logging method execution time.
 */
@Aspect
@Slf4j
public class LoggingTimeExecutionAspect {

    /**
     * Advice to log method execution time for methods within the kz.baltabayev.service package and its subpackages.
     * @param pjp The ProceedingJoinPoint representing the method being logged.
     * @return The result of the method execution.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around("execution(* org.beko.service..*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable{
        var methodSignature = (MethodSignature) pjp.getSignature();

        final var stopWatch = new StopWatch();

        stopWatch.start();
        var result = pjp.proceed();
        stopWatch.stop();

        log.info(
                "%s.%s :: %d ms".formatted(
                        methodSignature.getDeclaringType().getSimpleName(),
                        methodSignature.getName(),
                        stopWatch.getTime(TimeUnit.MILLISECONDS)
                )
        );

        return result;
    }
}
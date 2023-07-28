package com.momentum.investments.momentformgeneratorservice.oap;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class BasicPerformanceMonitor {
    @Around("@annotation(IPerformanceMonitor)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long resultTime = System.currentTimeMillis();

        long executionTime = resultTime - startTime;

        log.info("********* Performance Monitor: " + joinPoint.getSignature() +"*********");
        log.info(" executed in " + executionTime + "ms");
        log.info("**************************");
        return proceed;
    }
}

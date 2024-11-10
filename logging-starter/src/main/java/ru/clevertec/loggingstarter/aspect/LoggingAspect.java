package ru.clevertec.loggingstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* ru.clevertec.core.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logRequest(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        String argsString = args.length > 0 ? Arrays.toString(args) : "No arguments";

        log.info("Received request to method: {} with arguments: {}", methodName, argsString);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logResponse(Object result) {
        log.info("Response: {}", result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logException(Exception exception) {
        log.error("Exception occurred: {}", exception.getMessage(), exception);
    }
}


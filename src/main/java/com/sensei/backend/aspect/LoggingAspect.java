package com.sensei.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import com.sensei.backend.exception.SubscriptionException;
import com.sensei.backend.exception.ValidationException;

/**
 * Aspect for centralized logging of all Controller and Service methods.
 *
 * @author vaishnav88sk
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut that matches all endpoints in the controller package.
     */
    @Pointcut("within(com.sensei.backend.controller..*)")
    public void controllerPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut that matches all services in the service package.
     */
    @Pointcut("within(com.sensei.backend.service..*)")
    public void servicePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Advice that logs when a method is entered and exited.
     * Uses DEBUG level to prevent log bloat in production.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable if method throws exception
     */
    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            
            long elapsedTime = System.currentTimeMillis() - start;
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() executed in {} ms", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), elapsedTime);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Advice that logs methods throwing exceptions.
     * Uses ERROR level so it is always captured in production.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (e instanceof SubscriptionException || e instanceof ValidationException) {
            log.warn("Business Exception in {}.{}() with message = '{}'",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            return;
        }

        log.error("Exception in {}.{}() with cause = '{}' and message = '{}'",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage(),
                e);
    }
}

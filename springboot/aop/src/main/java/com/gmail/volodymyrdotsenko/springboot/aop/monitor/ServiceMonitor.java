package com.gmail.volodymyrdotsenko.springboot.aop.monitor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

/**
 * Created by Volodymyr Dotsenko on 6/20/16.
 */
@Aspect
@Component
public class ServiceMonitor {

    @AfterReturning("execution(* com..*Service.*(..))")
    public void logServiceAccess(JoinPoint joinPoint) {
        System.out.println("Completed: " + joinPoint);
    }
}
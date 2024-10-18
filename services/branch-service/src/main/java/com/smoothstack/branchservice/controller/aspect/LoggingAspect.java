package com.smoothstack.branchservice.controller.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.smoothstack.branchservice.controller.BranchController.createBranch(..))")
    public void logCreateBranch(JoinPoint joinPoint) {  // specified from logBefore
        logCommon(joinPoint);
    }

    @Before("execution(* com.smoothstack.branchservice.controller.BranchController.updateBranch(..))")
    public void logUpdateBranch(JoinPoint joinPoint) {
        logCommon(joinPoint);
    }

    @Before("execution(* com.smoothstack.branchservice.controller.BranchController.deleteBranchById(..))")
    public void logDeleteBranch(JoinPoint joinPoint) {
        logCommon(joinPoint);
    }

    private void logCommon(JoinPoint joinPoint) {
        logger.info("Request received at {}: {}", LocalDateTime.now(), getRequestURL());
        logger.info("User Identity: {}", getCurrentUser());
        logger.info("Method: {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("Request Parameters: {}", Arrays.toString(joinPoint.getArgs()));
    }

    private String getRequestURL() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRequestURL().toString();
    }

    private String getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("username");  // what else do we want to extract for the logging?
    }
}
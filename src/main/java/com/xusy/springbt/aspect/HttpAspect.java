package com.xusy.springbt.aspect;

import org.apache.log4j.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 廖师兄
 * 2017-01-15 12:31
 */
@Aspect
@Component
public class HttpAspect {

    private static Logger logger = Logger.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.xusy.springbt.controller.HelloController.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //url
        logger.info(String.format("url=%s", request.getRequestURL().toString()));

        //method
        logger.info(String.format("method=%s", request.getMethod()));

        //ip
        logger.info(String.format("ip=%s", request.getRemoteAddr()));

        //类方法
        logger.info(String.format("class_method=%s", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()));

        //参数
        logger.info(String.format("args=" + joinPoint.getArgs()));
    }

    @After("log()")
    public void doAfter() {
        logger.info("222222222222");
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        logger.info(String.format("response=%s", object.toString()));
    }

}

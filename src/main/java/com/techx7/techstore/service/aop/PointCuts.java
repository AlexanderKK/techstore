package com.techx7.techstore.service.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {

    @Pointcut("execution(* com.techx7.techstore.service.UserService.getAllUsers(..))")
    public void trackUsers() {}

    @Pointcut("@annotation(UserLoginsAOP)")
    public void userLoginsAOP() {}

}

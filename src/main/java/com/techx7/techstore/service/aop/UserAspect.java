package com.techx7.techstore.service.aop;

import com.techx7.techstore.service.MonitoringService;
import com.techx7.techstore.service.impl.MonitoringServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
public class UserAspect {

    private final MonitoringService monitoringService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringServiceImpl.class);

    @Autowired
    public UserAspect(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Before("PointCuts.trackUsers()")
    public void trackUsers() {
        monitoringService.getUsersLogins();
    }

    @Around("PointCuts.userLoginsAOP()")
    public Object logDiffInMilliseconds(ProceedingJoinPoint pjp) throws Throwable {
        UserLoginsAOP annotation = getAnnotation(pjp);

        long timeout = annotation.timeInMilliseconds();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        var proceedValue = pjp.proceed();

        stopWatch.stop();

        LOGGER.warn("The method's {} execution time duration was {} ms instead of {} ms",
                pjp.getSignature(), stopWatch.getLastTaskTimeMillis(), timeout);

        return proceedValue;
    }

    public static UserLoginsAOP getAnnotation(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        try {
            return pjp.getTarget()
                    .getClass()
                    .getMethod(method.getName(), method.getParameterTypes())
                    .getAnnotation(UserLoginsAOP.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}

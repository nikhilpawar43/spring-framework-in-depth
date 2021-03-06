package com.frankmoley.lil.fid.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {

    }

    //@Before("executeLogging()")
    //@AfterReturning(value = "executeLogging()", returning = "returnValue")
    @Around(value = "executeLogging()")
    public Object executeMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object returnValue = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;

        StringBuilder message = new StringBuilder("LoggingAspect - ");
        String methodName = joinPoint.getSignature().getName();
        message.append(methodName);

        message.append(", totalTime: ").append(totalTime).append("ms ");

        Object[] args = joinPoint.getArgs();

        if (Objects.nonNull(args) && args.length > 0) {
            message.append("[ | ");

            Arrays.stream(args).forEach(arg -> message.append(arg).append(" | "));

            message.append("]");
        }

        if (returnValue instanceof Collection) {
            message.append(", returning: ").append(((Collection) returnValue).size()).append(" instance(s)");
        } else {
            message.append(", returning: ").append(returnValue.toString());
        }

        LOGGER.info(message.toString());
        return returnValue;
    }
}

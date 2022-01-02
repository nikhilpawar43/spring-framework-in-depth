package com.frankmoley.lil.fid.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class CountingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountingAspect.class);

    private Map<String, Integer> methodCallCountMap = new HashMap<>();

    @Pointcut(value = "@annotation(Countable)")
    public void executeCounting() {

    }

    @Before(value = "executeCounting()")
    public void executeMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        Integer methodCallCount = methodCallCountMap.getOrDefault(methodName, 0);
        methodCallCountMap.put(methodName, ++methodCallCount);

        StringBuilder message = new StringBuilder();
        message.append("Counting Aspect - ");
        message.append("Method name: ").append(methodName).append(" ");
        message.append("has been called ").append(methodCallCountMap.getOrDefault(methodName, 0)).append(" time(s)");

        LOGGER.info(message.toString());
    }
}

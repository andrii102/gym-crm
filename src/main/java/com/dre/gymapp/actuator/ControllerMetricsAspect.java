package com.dre.gymapp.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerMetricsAspect {

    private final MeterRegistry meterRegistry;

    public ControllerMetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @After("execution(* com.dre.gymapp.controller..*(..))")
    public void incrementControllerMetric(JoinPoint joinPoint) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        meterRegistry.counter("gymapp.controller.invocations",
                "controller", controllerName,
                "method", methodName
        ).increment();

    }

    @AfterThrowing(
            pointcut = "execution(* com.dre.gymapp.controller..*(..))",
            throwing = "ex"
    )
    public void countControllerExceptions(JoinPoint joinPoint, Exception ex) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        meterRegistry.counter("gymapp.controller.exceptions",
                "controller", controllerName,
                "method", methodName,
                "exception", ex.getClass().getSimpleName()
        ).increment();
    }
}

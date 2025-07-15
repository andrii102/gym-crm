package com.dre.gymapp.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class RestLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(RestLoggingAspect.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public RestLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.dre.gymapp.controller..*(..))")
    public Object logRestRequests(ProceedingJoinPoint pjp) throws Throwable {
        // Get the current HTTP request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Extract HTTP method and URI
        String method = request.getMethod();
        String path = request.getRequestURI();

        // Convert method arguments to JSON string, excluding HTTP objects
        String requestBody = Arrays.stream(pjp.getArgs())
                .filter(arg -> !(arg instanceof HttpServletRequest ||
                        arg instanceof HttpServletResponse || arg instanceof BindingResult))
                .map(arg -> {
                    try {
                        return objectMapper.writeValueAsString(arg);
                    } catch (JsonProcessingException e) {
                        return "[Unserializable Object]";
                    }
                }).collect(Collectors.joining(", "));

        // Log the incoming request
        log.info("{} {} | RequestBody: {}", path, method, requestBody);

        try {
            // Execute the intercepted method
            Object result = pjp.proceed();
            // Log the response
            log.info("{} {} | Response: {}", path, method, objectMapper.writeValueAsString(result));
            return result;
        } catch (Exception e) {
            log.error("{} {} | Error: {}", path, method, e.getMessage());
            throw e;
        }
    }

}

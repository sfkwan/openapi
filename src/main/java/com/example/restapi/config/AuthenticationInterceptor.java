package com.example.restapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.restapi.exception.AuthenticationException;
import com.example.restapi.annotation.VerifyToken;

import org.springframework.web.method.HandlerMethod;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(VerifyToken.class)) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() <= 7) {
                throw new AuthenticationException("jwt-notvalid-0401", "Missing or invalid Authorization header");
            }
            // Optionally, add JWT validation logic here
        }
        return true;
    }
}

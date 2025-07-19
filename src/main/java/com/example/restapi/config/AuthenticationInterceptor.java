package com.example.restapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.restapi.exception.AuthenticationException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.restapi.annotation.VerifyToken;

import org.springframework.web.method.HandlerMethod;

@Component
@Slf4j

public class AuthenticationInterceptor implements HandlerInterceptor {

    // Simple User class for JWT payload mapping
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public String name;
        public int sub;
        // public long iat;
        // public long exp;

        @Override
        public String toString() {
            return "User{name='" + name + "', sub=" + sub + "}";
            // ", iat=" + iat + ", exp=" + exp +
        }
    }

    @Override
    public boolean preHandle(@org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull Object handler)
            throws Exception {
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            StringBuilder headersLog = new StringBuilder("Request Headers:\n");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headersLog.append(headerName).append(": ").append(headerValue).append("\n");
            }
            log.error(headersLog.toString());
        }

        if (handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(VerifyToken.class)) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() <= 7) {
                throw new AuthenticationException("jwt-notvalid-0401", "Missing or invalid Authorization header");
            } else {
                String token = authHeader.substring(7);
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    // Check if all parts are base64url decodable
                    try {
                        for (String part : parts) {
                            java.util.Base64.getUrlDecoder().decode(part);
                        }
                    } catch (IllegalArgumentException e) {
                        log.error("Token parts are not valid base64url: {}", e.getMessage());
                        throw new AuthenticationException("jwt-notvalid-0401", "Malformed JWT token");
                    }
                    // If here, it's a valid JWT structure
                    try {
                        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                        // Parse payload into User object
                        try {
                            User user = new com.fasterxml.jackson.databind.ObjectMapper().readValue(payload,
                                    User.class);
                            log.error("JWT User object: {}", user);
                        } catch (Exception jsonEx) {
                            log.error("Failed to parse JWT payload as User: {}", jsonEx.getMessage());
                        }
                    } catch (Exception e) {
                        log.error("Failed to decode JWT token: {}", e.getMessage());
                    }
                } else {
                    log.error("Invalid JWT token format");
                    throw new AuthenticationException("jwt-notvalid-0401", "Malformed JWT token");
                }
            }

            // Optionally, add JWT validation logic here
        }
        return true;
    }
}
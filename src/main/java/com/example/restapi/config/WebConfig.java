package com.example.restapi.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(servers = {
        // @Server(url = "https://api.example.com", description = "Production server"),
        // @Server(url = "https://staging-api.example.com", description = "Staging
        // server"),
        @Server(url = "http://localhost:8080", description = "Local server")
})
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER, description = "JWT Bearer Token Authentication")
@SecurityScheme(name = "cookieAuth", type = SecuritySchemeType.APIKEY, bearerFormat = "JWT", in = SecuritySchemeIn.COOKIE, paramName = "access_token", description = "Session Cookie Authentication")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/books"); // Only intercept /api/books (getAllBooks)
    }

}

package com.example.restapi.exception;

public class AuthenticationException extends ApiException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String code, String message) {
        super(code, message);
    }
}

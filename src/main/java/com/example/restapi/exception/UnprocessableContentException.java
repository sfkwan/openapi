package com.example.restapi.exception;

public class UnprocessableContentException extends ApiException {
    public UnprocessableContentException(String message) {
        super(message);
    }

    public UnprocessableContentException(String code, String message) {
        super(code, message);
    }
}

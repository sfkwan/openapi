package com.example.restapi.exception;

/**
 * Custom exception class for API-related errors.
 * Encapsulates an error code and a message for detailed error reporting.
 */
public class ApiException extends RuntimeException {
    private final String code;
    private final String message;

    public ApiException(String message) {
        super(message);
        this.code = "";
        this.message = message;
    }

    /**
     * Constructs a new ApiException with the specified error code and detail
     * message.
     *
     * @param code    the error code associated with this exception
     * @param message the detail message for this exception
     */
    public ApiException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

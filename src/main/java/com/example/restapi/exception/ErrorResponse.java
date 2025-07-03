package com.example.restapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Represents an error response containing a code and a message.
 */
public class ErrorResponse {

    private String code;
    private String message;

}
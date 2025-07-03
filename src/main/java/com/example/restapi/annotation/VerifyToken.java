package com.example.restapi.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Annotation to indicate that a method requires token verification.
 */
public @interface VerifyToken {
}

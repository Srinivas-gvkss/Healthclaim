package com.gvkss.patil.user_service.exception;

/**
 * Custom exception for handling validation errors.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
public class ValidationException extends RuntimeException {
    
    /**
     * Constructor with message
     * 
     * @param message The error message
     */
    public ValidationException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message The error message
     * @param cause The cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

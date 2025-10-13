package com.gvkss.patil.user_service.exception;

/**
 * Custom exception for handling resource not found errors.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor with message
     * 
     * @param message The error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message The error message
     * @param cause The cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

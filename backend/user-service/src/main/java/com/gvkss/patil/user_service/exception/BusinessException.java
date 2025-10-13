package com.gvkss.patil.user_service.exception;

/**
 * Custom business exception for handling business logic errors.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
public class BusinessException extends RuntimeException {
    
    private final int statusCode;
    
    /**
     * Constructor with message
     * 
     * @param message The error message
     */
    public BusinessException(String message) {
        super(message);
        this.statusCode = 400;
    }
    
    /**
     * Constructor with message and status code
     * 
     * @param message The error message
     * @param statusCode The HTTP status code
     */
    public BusinessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message The error message
     * @param cause The cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 400;
    }
    
    /**
     * Constructor with message, cause and status code
     * 
     * @param message The error message
     * @param cause The cause
     * @param statusCode The HTTP status code
     */
    public BusinessException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    /**
     * Get the HTTP status code
     * 
     * @return The status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}

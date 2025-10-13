package com.gvkss.patil.user_service.exception;

import com.gvkss.patil.user_service.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for handling all exceptions across the application.
 * Provides centralized error handling and consistent error responses.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Handle validation errors
     * 
     * @param ex The validation exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        log.warn("Validation error occurred: {}", ex.getMessage());
        
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });
        
        ApiResponse<Object> response = ApiResponse.validationError(errors);
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Handle runtime exceptions
     * 
     * @param ex The runtime exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Handle illegal argument exceptions
     * 
     * @param ex The illegal argument exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        log.warn("Illegal argument exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(), 
                HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Handle authentication exceptions
     * 
     * @param ex The authentication exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        
        log.warn("Authentication exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.unauthorized(
                "Authentication failed: " + ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handle bad credentials exceptions
     * 
     * @param ex The bad credentials exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        
        log.warn("Bad credentials exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.unauthorized(
                "Invalid credentials provided"
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handle access denied exceptions
     * 
     * @param ex The access denied exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        log.warn("Access denied exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.forbidden(
                "Access denied: " + ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    /**
     * Handle data integrity violation exceptions
     * 
     * @param ex The data integrity violation exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        
        log.error("Data integrity violation exception occurred: {}", ex.getMessage(), ex);
        
        String message = "Data integrity violation occurred";
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("duplicate key")) {
                message = "Duplicate entry found";
            } else if (ex.getMessage().contains("foreign key constraint")) {
                message = "Referenced entity does not exist";
            } else if (ex.getMessage().contains("not null constraint")) {
                message = "Required field is missing";
            }
        }
        
        ApiResponse<Object> response = ApiResponse.error(
                message, 
                HttpStatus.CONFLICT.value()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Handle generic exceptions
     * 
     * @param ex The exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        
        ApiResponse<Object> response = ApiResponse.error(
                "An unexpected error occurred. Please try again later.", 
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Handle custom business exceptions
     * 
     * @param ex The business exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        log.warn("Business exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(), 
                ex.getStatusCode()
        );
        
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    
    /**
     * Handle resource not found exceptions
     * 
     * @param ex The resource not found exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        log.warn("Resource not found exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.notFound(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handle validation exceptions
     * 
     * @param ex The validation exception
     * @param request The web request
     * @return Error response
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            ValidationException ex, WebRequest request) {
        
        log.warn("Validation exception occurred: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(), 
                HttpStatus.BAD_REQUEST.value()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
}

package com.gvkss.patil.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Generic API response wrapper for consistent response structure.
 * Provides standardized response format for all API endpoints.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * Response status (success, error, warning)
     */
    private String status;
    
    /**
     * HTTP status code
     */
    private int statusCode;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Response data
     */
    private T data;
    
    /**
     * List of errors (if any)
     */
    private List<String> errors;
    
    /**
     * Response timestamp
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Request ID for tracking
     */
    private String requestId;
    
    /**
     * Additional metadata
     */
    private Object metadata;
    
    /**
     * Create success response with data
     * 
     * @param data The response data
     * @param <T> The data type
     * @return Success response
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(200)
                .message("Operation completed successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create success response with custom message
     * 
     * @param data The response data
     * @param message The custom message
     * @param <T> The data type
     * @return Success response
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(200)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create success response with custom status code
     * 
     * @param data The response data
     * @param message The custom message
     * @param statusCode The HTTP status code
     * @param <T> The data type
     * @return Success response
     */
    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create error response
     * 
     * @param message The error message
     * @param statusCode The HTTP status code
     * @param <T> The data type
     * @return Error response
     */
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(statusCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create error response with errors list
     * 
     * @param message The error message
     * @param errors The list of errors
     * @param statusCode The HTTP status code
     * @param <T> The data type
     * @return Error response
     */
    public static <T> ApiResponse<T> error(String message, List<String> errors, int statusCode) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(statusCode)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create validation error response
     * 
     * @param errors The list of validation errors
     * @param <T> The data type
     * @return Validation error response
     */
    public static <T> ApiResponse<T> validationError(List<String> errors) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(400)
                .message("Validation failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create not found response
     * 
     * @param message The not found message
     * @param <T> The data type
     * @return Not found response
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(404)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create unauthorized response
     * 
     * @param message The unauthorized message
     * @param <T> The data type
     * @return Unauthorized response
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(401)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create forbidden response
     * 
     * @param message The forbidden message
     * @param <T> The data type
     * @return Forbidden response
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(403)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create created response
     * 
     * @param data The created data
     * @param message The success message
     * @param <T> The data type
     * @return Created response
     */
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(201)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create no content response
     * 
     * @param message The success message
     * @param <T> The data type
     * @return No content response
     */
    public static <T> ApiResponse<T> noContent(String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(204)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

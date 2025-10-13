package com.gvkss.patil.user_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Entry Point for handling unauthorized access attempts.
 * Returns a proper JSON response when authentication is required.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Commence an authentication scheme.
     * Called when an authentication is required but not provided.
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @param authException The authentication exception
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        log.warn("Unauthorized access attempt to: {} from IP: {}", 
                request.getRequestURI(), getClientIpAddress(request));
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Authentication required to access this resource");
        body.put("path", request.getRequestURI());
        body.put("method", request.getMethod());
        
        // Add additional error details based on the exception type
        if (authException != null) {
            body.put("errorType", authException.getClass().getSimpleName());
            body.put("errorMessage", authException.getMessage());
        }
        
        // Add request information
        body.put("requestId", request.getHeader("X-Request-ID"));
        body.put("userAgent", request.getHeader("User-Agent"));
        
        // Add security headers
        body.put("securityInfo", Map.of(
                "requiresAuthentication", true,
                "authenticationType", "JWT",
                "tokenRequired", true
        ));
        
        // Write the response
        objectMapper.writeValue(response.getOutputStream(), body);
        
        log.debug("Unauthorized response sent for request: {}", request.getRequestURI());
    }
    
    /**
     * Get the client IP address from the request
     * 
     * @param request The HTTP request
     * @return The client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        String xForwardedProto = request.getHeader("X-Forwarded-Proto");
        if (xForwardedProto != null && !xForwardedProto.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedProto)) {
            return xForwardedProto;
        }
        
        return request.getRemoteAddr();
    }
}

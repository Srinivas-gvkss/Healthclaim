package com.gvkss.patil.api_gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * CORS Configuration for API Gateway
 * 
 * Note: CORS is now configured via application.yml globalcors settings
 * to avoid conflicts with Spring Cloud Gateway
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class CorsConfig {
    // CORS configuration moved to application.yml globalcors section
    // This avoids conflicts with Spring Cloud Gateway's CORS handling
}

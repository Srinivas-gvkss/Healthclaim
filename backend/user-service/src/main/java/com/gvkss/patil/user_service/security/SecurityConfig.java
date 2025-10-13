package com.gvkss.patil.user_service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.gvkss.patil.user_service.security.Argon2PasswordEncoderConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for the User Service.
 * Configures JWT authentication, CORS, and security policies.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableCaching
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Argon2PasswordEncoderConfig argon2Config;
    
    /**
     * Configure the security filter chain
     * 
     * @param http The HTTP security configuration
     * @return The security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");
        
        http
            // Disable CSRF for JWT-based authentication
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS disabled - handled by API Gateway
            // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure session management to be stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure exception handling
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow all OPTIONS requests for CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Public endpoints
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/signup").permitAll()
                .requestMatchers("/auth/refresh").permitAll()
                .requestMatchers("/auth/forgot-password").permitAll()
                .requestMatchers("/auth/validate-token").permitAll()
                .requestMatchers("/auth/init-admin").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/roles/public/**").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers("/info").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                
                // Admin endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/users/**").hasAnyRole("ADMIN", "INSURANCE_PROVIDER")
                .requestMatchers("/roles/**").hasRole("ADMIN")
                .requestMatchers("/departments/**").hasRole("ADMIN")
                
                // Claims endpoints (will be created later)
                .requestMatchers("/claims/submit").hasRole("PATIENT")
                .requestMatchers("/claims/my-claims").hasRole("PATIENT")
                .requestMatchers("/claims/process/**").hasAnyRole("INSURANCE_PROVIDER", "ADMIN")
                .requestMatchers("/claims/verify/**").hasRole("DOCTOR")
                .requestMatchers("/claims/**").hasAnyRole("PATIENT", "DOCTOR", "INSURANCE_PROVIDER", "ADMIN")
                
                // Policies endpoints (will be created later)
                .requestMatchers("/policies/**").hasAnyRole("PATIENT", "INSURANCE_PROVIDER", "ADMIN")
                
                // User profile endpoints
                .requestMatchers("/profile/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Add JWT authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        log.info("Security filter chain configured successfully");
        return http.build();
    }
    
    /**
     * Configure CORS
     * 
     * @return CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configuring CORS");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:5173",  // Vite dev server
                "http://localhost:8080",
                "https://*.yourdomain.com"
        ));
        
        // Allow specific methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Allow specific headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-Request-ID"
        ));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Expose headers
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Request-ID"
        ));
        
        // Set max age
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS configured successfully");
        return source;
    }
    
    /**
     * Configure password encoder
     * 
     * @return Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configuring Argon2 password encoder for optimal performance");
        // Argon2 is often 2-3x faster than BCrypt for equivalent security
        // Expected authentication time: ~200-400ms (vs 1759ms with BCrypt)
        return argon2Config.createArgon2Encoder();
    }
    
    /**
     * Configure authentication provider
     * 
     * @return Authentication provider bean
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("Configuring DAO authentication provider");
        
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }
    
    /**
     * Configure authentication manager
     * 
     * @param authConfig The authentication configuration
     * @return Authentication manager bean
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        log.info("Configuring authentication manager");
        return authConfig.getAuthenticationManager();
    }
}

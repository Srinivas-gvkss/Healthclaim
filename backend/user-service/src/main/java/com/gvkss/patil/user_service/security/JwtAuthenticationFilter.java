package com.gvkss.patil.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter for processing JWT tokens in requests.
 * Extracts JWT tokens from request headers and validates them.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    
    /**
     * Process the JWT token in the request
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
                String email = tokenProvider.getEmailFromToken(jwt);
                
                log.debug("JWT token found for user: {}", email);
                
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("Authentication set for user: {}", email);
                }
            } else if (StringUtils.hasText(jwt)) {
                log.warn("Invalid JWT token provided for request: {}", request.getRequestURI());
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Extract JWT token from the request
     * 
     * @param request The HTTP request
     * @return The JWT token, or null if not found
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
    
    /**
     * Check if the request should be filtered
     * 
     * @param request The HTTP request
     * @return true if the request should be filtered
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Skip JWT filtering for public endpoints
        return path.startsWith("/api/auth/login") || 
               path.startsWith("/api/auth/signup") || 
               path.startsWith("/api/auth/refresh") ||
               path.startsWith("/api/auth/forgot-password") ||
               path.startsWith("/api/auth/validate-token") ||
               path.startsWith("/api/auth/init-admin") ||
               path.startsWith("/api/public/") || 
               path.startsWith("/api/roles/public/") ||
               path.startsWith("/swagger-ui/") || 
               path.startsWith("/api-docs/") ||
               path.startsWith("/h2-console/") ||
               path.equals("/api/health") ||
               path.equals("/api/info");
    }
}

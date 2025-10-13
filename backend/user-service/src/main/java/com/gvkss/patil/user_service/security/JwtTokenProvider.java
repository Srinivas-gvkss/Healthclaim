package com.gvkss.patil.user_service.security;

import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT Token Provider for generating and validating JWT tokens.
 * Handles token creation, validation, and extraction of user information.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Component
@Slf4j
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    private final long jwtExpirationInMs;
    private final long refreshTokenExpirationInMs;
    
    /**
     * Constructor for JwtTokenProvider
     * 
     * @param jwtSecret The JWT secret key
     * @param jwtExpirationInMs JWT token expiration time in milliseconds
     * @param refreshTokenExpirationInMs Refresh token expiration time in milliseconds
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpirationInMs,
            @Value("${jwt.refresh.expiration}") long refreshTokenExpirationInMs) {
        
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.refreshTokenExpirationInMs = refreshTokenExpirationInMs;
        
        log.info("JWT Token Provider initialized with expiration: {}ms, refresh expiration: {}ms", 
                jwtExpirationInMs, refreshTokenExpirationInMs);
    }
    
    /**
     * Generate JWT access token from user authentication
     * 
     * @param authentication The authentication object
     * @return JWT access token
     */
    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateAccessToken(userPrincipal);
    }
    
    /**
     * Generate JWT access token from user principal
     * 
     * @param userPrincipal The user principal
     * @return JWT access token
     */
    public String generateAccessToken(UserPrincipal userPrincipal) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPrincipal.getId());
        claims.put("email", userPrincipal.getEmail());
        claims.put("username", userPrincipal.getUsername());
        claims.put("firstName", userPrincipal.getFirstName());
        claims.put("lastName", userPrincipal.getLastName());
        claims.put("roles", userPrincipal.getRoles());
        claims.put("permissions", userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("departmentId", userPrincipal.getDepartmentId());
        claims.put("departmentName", userPrincipal.getDepartmentName());
        claims.put("tokenType", "ACCESS");
        
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Generate JWT refresh token from user principal
     * 
     * @param userPrincipal The user principal
     * @return JWT refresh token
     */
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        Date expiryDate = new Date(System.currentTimeMillis() + refreshTokenExpirationInMs);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPrincipal.getId());
        claims.put("email", userPrincipal.getEmail());
        claims.put("tokenType", "REFRESH");
        
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Generate JWT access token from user entity
     * 
     * @param user The user entity
     * @return JWT access token
     */
    public String generateAccessToken(User user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return generateAccessToken(userPrincipal);
    }
    
    /**
     * Generate JWT refresh token from user entity
     * 
     * @param user The user entity
     * @return JWT refresh token
     */
    public String generateRefreshToken(User user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return generateRefreshToken(userPrincipal);
    }
    
    /**
     * Extract user ID from JWT token
     * 
     * @param token The JWT token
     * @return User ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * Extract email from JWT token
     * 
     * @param token The JWT token
     * @return Email address
     */
    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    /**
     * Extract username from JWT token
     * 
     * @param token The JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * Extract roles from JWT token
     * 
     * @param token The JWT token
     * @return List of roles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("roles", List.class);
    }
    
    /**
     * Extract permissions from JWT token
     * 
     * @param token The JWT token
     * @return List of permissions
     */
    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("permissions", List.class);
    }
    
    /**
     * Extract department ID from JWT token
     * 
     * @param token The JWT token
     * @return Department ID
     */
    public Long getDepartmentIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("departmentId", Long.class);
    }
    
    /**
     * Extract token type from JWT token
     * 
     * @param token The JWT token
     * @return Token type (ACCESS or REFRESH)
     */
    public String getTokenTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("tokenType", String.class);
    }
    
    /**
     * Extract expiration date from JWT token
     * 
     * @param token The JWT token
     * @return Expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
    
    /**
     * Check if JWT token is expired
     * 
     * @param token The JWT token
     * @return true if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }
    
    /**
     * Validate JWT token
     * 
     * @param token The JWT token
     * @return true if token is valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("JWT validation error: {}", ex.getMessage());
        }
        return false;
    }
    
    /**
     * Validate JWT token and check if it's an access token
     * 
     * @param token The JWT token
     * @return true if token is valid and is an access token
     */
    public boolean validateAccessToken(String token) {
        if (!validateToken(token)) {
            return false;
        }
        
        String tokenType = getTokenTypeFromToken(token);
        return "ACCESS".equals(tokenType);
    }
    
    /**
     * Validate JWT token and check if it's a refresh token
     * 
     * @param token The JWT token
     * @return true if token is valid and is a refresh token
     */
    public boolean validateRefreshToken(String token) {
        if (!validateToken(token)) {
            return false;
        }
        
        String tokenType = getTokenTypeFromToken(token);
        return "REFRESH".equals(tokenType);
    }
    
    /**
     * Extract claims from JWT token
     * 
     * @param token The JWT token
     * @return Claims object
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Get token expiration time in milliseconds
     * 
     * @return Token expiration time
     */
    public long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
    
    /**
     * Get refresh token expiration time in milliseconds
     * 
     * @return Refresh token expiration time
     */
    public long getRefreshTokenExpirationInMs() {
        return refreshTokenExpirationInMs;
    }
    
    /**
     * Create UserPrincipal from JWT token
     * 
     * @param token The JWT token
     * @return UserPrincipal object
     */
    public UserPrincipal createUserPrincipalFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        
        Long userId = claims.get("userId", Long.class);
        String email = claims.getSubject();
        String username = claims.get("username", String.class);
        String firstName = claims.get("firstName", String.class);
        String lastName = claims.get("lastName", String.class);
        Long departmentId = claims.get("departmentId", Long.class);
        String departmentName = claims.get("departmentName", String.class);
        
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        @SuppressWarnings("unchecked")
        List<String> permissions = claims.get("permissions", List.class);
        
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        return UserPrincipal.builder()
                .id(userId)
                .email(email)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .roles(roles)
                .authorities(authorities)
                .departmentId(departmentId)
                .departmentName(departmentName)
                .build();
    }
}

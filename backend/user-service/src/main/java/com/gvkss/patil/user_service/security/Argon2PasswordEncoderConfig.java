package com.gvkss.patil.user_service.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Argon2 Password Encoder - Modern alternative to BCrypt
 * More tunable and often faster than BCrypt for equivalent security
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Component
public class Argon2PasswordEncoderConfig {
    
    /**
     * Create Argon2 password encoder with optimized settings
     * 
     * @return Argon2 password encoder
     */
    public PasswordEncoder createArgon2Encoder() {
        // Argon2 parameters optimized for performance while maintaining security
        // memory: 16MB, iterations: 2, parallelism: 1
        // This should be much faster than BCrypt strength 9-10
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}

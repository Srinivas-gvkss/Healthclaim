package com.gvkss.patil.user_service.security;

import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom UserDetailsService implementation for Spring Security.
 * Loads user details from the database for authentication.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    /**
     * Load user details by username (email)
     * 
     * @param username The username (email) to load
     * @return UserDetails object
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        
        // Use optimized query to load user with all related data in a single query
        User user = userRepository.findByEmailOrUsernameWithRolesAndDepartment(username, username)
                .orElseThrow(() -> {
                    log.warn("User not found with username or email: {}", username);
                    return new UsernameNotFoundException("User not found with username or email: " + username);
                });
        
        log.debug("User found: {} with status: {}", user.getEmail(), user.getStatus());
        
        // Check if user can access the system
        if (!user.canAccess()) {
            log.warn("User {} cannot access the system. Status: {}, Enabled: {}, AccountNonLocked: {}", 
                    user.getEmail(), user.getStatus(), user.getEnabled(), user.getAccountNonLocked());
            throw new UsernameNotFoundException("User account is not accessible: " + username);
        }
        
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        log.debug("UserPrincipal created for user: {} with roles: {}", user.getEmail(), userPrincipal.getRoles());
        
        return userPrincipal;
    }
    
    /**
     * Load user details by user ID
     * 
     * @param userId The user ID to load
     * @return UserDetails object
     * @throws UsernameNotFoundException if user is not found
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        log.debug("Loading user by ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new UsernameNotFoundException("User not found with ID: " + userId);
                });
        
        log.debug("User found: {} with status: {}", user.getEmail(), user.getStatus());
        
        // Check if user can access the system
        if (!user.canAccess()) {
            log.warn("User {} cannot access the system. Status: {}, Enabled: {}, AccountNonLocked: {}", 
                    user.getEmail(), user.getStatus(), user.getEnabled(), user.getAccountNonLocked());
            throw new UsernameNotFoundException("User account is not accessible: " + user.getEmail());
        }
        
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        log.debug("UserPrincipal created for user: {} with roles: {}", user.getEmail(), userPrincipal.getRoles());
        
        return userPrincipal;
    }
    
    /**
     * Load user details by username or email
     * 
     * @param usernameOrEmail The username or email to load
     * @return UserDetails object
     * @throws UsernameNotFoundException if user is not found
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
        log.debug("Loading user by username or email: {}", usernameOrEmail);
        
        // Use optimized query to load user with all related data in a single query
        User user = userRepository.findByEmailOrUsernameWithRolesAndDepartment(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    log.warn("User not found with username or email: {}", usernameOrEmail);
                    return new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
                });
        
        log.debug("User found: {} with status: {}", user.getEmail(), user.getStatus());
        
        // Check if user can access the system
        if (!user.canAccess()) {
            log.warn("User {} cannot access the system. Status: {}, Enabled: {}, AccountNonLocked: {}", 
                    user.getEmail(), user.getStatus(), user.getEnabled(), user.getAccountNonLocked());
            throw new UsernameNotFoundException("User account is not accessible: " + usernameOrEmail);
        }
        
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        log.debug("UserPrincipal created for user: {} with roles: {}", user.getEmail(), userPrincipal.getRoles());
        
        return userPrincipal;
    }
    
    /**
     * Check if user exists by email
     * 
     * @param email The email to check
     * @return true if user exists
     */
    @Transactional(readOnly = true)
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Check if user exists by username
     * 
     * @param username The username to check
     * @return true if user exists
     */
    @Transactional(readOnly = true)
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Get user by email
     * 
     * @param email The email to search for
     * @return User entity
     * @throws UsernameNotFoundException if user is not found
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    
    /**
     * Get user by username
     * 
     * @param username The username to search for
     * @return User entity
     * @throws UsernameNotFoundException if user is not found
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
    /**
     * Get user by ID
     * 
     * @param userId The user ID to search for
     * @return User entity
     * @throws UsernameNotFoundException if user is not found
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }
}

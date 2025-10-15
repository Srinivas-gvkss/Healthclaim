package com.gvkss.patil.claim_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Healthcare Claim Service Application
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ClaimServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ClaimServiceApplication.class, args);
    }
}

package com.gvkss.patil.notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Healthcare Notification Service Application
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}

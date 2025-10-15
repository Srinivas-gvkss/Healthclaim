package com.gvkss.patil.reporting_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Healthcare Reporting Service Application
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ReportingServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ReportingServiceApplication.class, args);
    }
}

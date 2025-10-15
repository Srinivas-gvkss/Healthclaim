package com.gvkss.patil.patient_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Healthcare Patient Service Application
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PatientServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}

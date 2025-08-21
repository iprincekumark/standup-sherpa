package com.standup.sherpa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class DescopeConfig {

    @Value("${descope.project.id:}")
    private String projectId;

    @Value("${descope.management.key:}")
    private String managementKey;

    @PostConstruct
    public void validateConfiguration() {
        System.out.println("=== Descope Configuration Check ===");
        System.out.println("Project ID: " + (isValidProperty(projectId) ? "✓ Set" : "✗ Missing/Empty"));
        System.out.println("Management Key: " + (isValidProperty(managementKey) ? "✓ Set" : "✗ Missing/Empty"));
        
        if (!isValidProperty(projectId)) {
            System.err.println("ERROR: DESCOPE_PROJECT_ID is not set!");
            System.err.println("Make sure your .env file contains: DESCOPE_PROJECT_ID=your_project_id");
        }
        
        if (!isValidProperty(managementKey)) {
            System.err.println("ERROR: DESCOPE_MANAGEMENT_KEY is not set!");
            System.err.println("Make sure your .env file contains: DESCOPE_MANAGEMENT_KEY=your_management_key");
        }
    }

    @Bean
    public DescopeClientWrapper descopeClient() {
        // For tests, allow empty values and create a mock client
        if (!isValidProperty(projectId) || !isValidProperty(managementKey)) {
            System.out.println("⚠️  Creating mock Descope client for testing/development");
            return new DescopeClientWrapper("mock-project-id", "mock-management-key");
        }

        System.out.println("✅ Creating Descope client with project ID: " + projectId.substring(0, Math.min(8, projectId.length())) + "...");
        return new DescopeClientWrapper(projectId, managementKey);
    }
    
    private boolean isValidProperty(String value) {
        return value != null && 
               !value.isEmpty() && 
               !value.startsWith("${") && 
               !value.equals("mock-project-id") && 
               !value.equals("mock-management-key");
    }
}
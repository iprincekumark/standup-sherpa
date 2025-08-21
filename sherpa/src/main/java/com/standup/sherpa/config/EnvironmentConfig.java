package com.standup.sherpa.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
public class EnvironmentConfig {
    
    static {
        // Load environment variables as early as possible
        try {
            Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
            
            dotenv.entries().forEach(entry -> {
                // Only set if not already present (allows override)
                if (System.getProperty(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
        } catch (Exception e) {
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
        }
    }
    
    @PostConstruct
    public void logEnvironmentStatus() {
        System.out.println("Environment variables loaded:");
        System.out.println("DESCOPE_PROJECT_ID: " + (System.getProperty("DESCOPE_PROJECT_ID") != null ? "✓ Set" : "✗ Missing"));
        System.out.println("DESCOPE_MANAGEMENT_KEY: " + (System.getProperty("DESCOPE_MANAGEMENT_KEY") != null ? "✓ Set" : "✗ Missing"));
        System.out.println("DESCOPE_JWT_SECRET: " + (System.getProperty("DESCOPE_JWT_SECRET") != null ? "✓ Set" : "✗ Missing"));
    }
}
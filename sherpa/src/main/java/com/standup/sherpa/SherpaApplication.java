package com.standup.sherpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SherpaApplication {

    public static void main(String[] args) {
        // Load environment variables from .env file BEFORE Spring starts
        try {
            Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Don't fail if .env file doesn't exist (useful for tests)
                .load();
            
            // Set system properties so Spring can access them
            dotenv.entries().forEach(entry -> {
                // Only set if not already present (allows command line overrides)
                if (System.getProperty(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
            
            System.out.println("✅ Environment variables loaded from .env file");
            
        } catch (Exception e) {
            System.out.println("⚠️  Could not load .env file: " + e.getMessage() + " (continuing with system environment)");
            // Continue anyway - might be running in production with actual env vars
        }
        
        // Start Spring Boot application
        SpringApplication.run(SherpaApplication.class, args);
    }
}
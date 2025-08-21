package com.standup.sherpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "descope.project.id=test-project-id",
    "descope.management.key=test-management-key", 
    "descope.jwt.secret=test-jwt-secret-that-is-long-enough-for-hmac256-algorithm",
    "slack.client.id=test-slack-client",
    "slack.client.secret=test-slack-secret",
    "github.client.id=test-github-client", 
    "github.client.secret=test-github-secret",
    "github.token=test-github-token",
    "google.calendar.api.key=test-google-api-key",
    "google.client.id=test-google-client",
    "google.client.secret=test-google-secret"
})
class SherpaApplicationTests {

    @Test
    void contextLoads() {
        // Test that Spring context loads successfully
        System.out.println("✅ Spring Boot context loaded successfully!");
    }
}
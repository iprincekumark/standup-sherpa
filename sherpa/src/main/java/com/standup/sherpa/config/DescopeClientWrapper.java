package com.standup.sherpa.config;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Wrapper for Descope operations to avoid direct SDK dependency issues
 */
public class DescopeClientWrapper {

    private final String projectId;
    private final String managementKey;
    private final RestTemplate restTemplate;
    private static final String DESCOPE_BASE_URL = "https://api.descope.com";

    public DescopeClientWrapper(String projectId, String managementKey) {
        this.projectId = projectId;
        this.managementKey = managementKey;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Generate OAuth URL for outbound app integration
     */
    public String createOAuthUrl(String provider, String redirectUri) {
        // This is a placeholder - implement based on Descope's REST API
        return String.format("https://auth.descope.io/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=openid profile email",
                projectId, redirectUri);
    }

    /**
     * Exchange authorization code for access token
     */
    public TokenResponse exchangeCodeForToken(String code, String redirectUri) {
        // Placeholder implementation - replace with actual Descope API call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + managementKey);
        headers.set("Content-Type", "application/json");

        Map<String, String> requestBody = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", redirectUri
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    DESCOPE_BASE_URL + "/v1/oauth2/token",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            return new TokenResponse(
                    (String) body.get("access_token"),
                    (String) body.get("refresh_token"),
                    (Integer) body.get("expires_in")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to exchange code for token: " + e.getMessage(), e);
        }
    }

    public static class TokenResponse {
        private final String accessToken;
        private final String refreshToken;
        private final Integer expiresIn;

        public TokenResponse(String accessToken, String refreshToken, Integer expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }

        // Getters
        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public Integer getExpiresIn() { return expiresIn; }
    }
}
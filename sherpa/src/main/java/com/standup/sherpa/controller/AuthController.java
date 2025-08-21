package com.standup.sherpa.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.standup.sherpa.config.DescopeClientWrapper;
import com.standup.sherpa.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** 
     * Validate JWT token 
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractBearerToken(authHeader);
            DecodedJWT jwt = authService.validateToken(token);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("userId", authService.getUserId(jwt));
            response.put("email", authService.getEmail(jwt));
            response.put("expiresAt", authService.getExpiration(jwt));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid token"));
        }
    }

    /** 
     * Get OAuth URL for provider 
     */
    @GetMapping("/oauth/{provider}")
    public ResponseEntity<Map<String, String>> getOAuthUrl(@PathVariable String provider,
            @RequestParam(defaultValue = "http://localhost:8080/api/auth/callback") String redirectUri) {
        try {
            String oauthUrl = authService.getOAuthUrl(provider, redirectUri);
            return ResponseEntity.ok(Map.of("authUrl", oauthUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** 
     * Handle OAuth callback 
     */
    @GetMapping("/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestParam String code,
            @RequestParam(defaultValue = "http://localhost:8080/api/auth/callback") String redirectUri) {
        try {
            DescopeClientWrapper.TokenResponse tokenResponse = authService.exchangeCode(code, redirectUri);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("accessToken", tokenResponse.getAccessToken());
            response.put("expiresIn", tokenResponse.getExpiresIn());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    private String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authHeader.substring(7).trim();
    }
}
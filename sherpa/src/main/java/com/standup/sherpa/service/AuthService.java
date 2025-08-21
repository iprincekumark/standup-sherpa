package com.standup.sherpa.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import com.standup.sherpa.config.DescopeClientWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthService {

    private final DescopeClientWrapper descopeClient;
    private final JWTVerifier verifier;

    public AuthService(DescopeClientWrapper descopeClient,
                      @Value("${descope.jwt.secret:test-jwt-secret-that-is-long-enough-for-hmac256-algorithm}") String jwtSecret,
                      @Value("${descope.project.id:test-project-id}") String projectId) {
        this.descopeClient = descopeClient;

        // Initialize JWT verifier with fallback values for testing
        String secretToUse = (jwtSecret != null && !jwtSecret.isEmpty() && !jwtSecret.startsWith("${")) 
            ? jwtSecret 
            : "test-jwt-secret-that-is-long-enough-for-hmac256-algorithm";
            
        String issuerToUse = (projectId != null && !projectId.isEmpty() && !projectId.startsWith("${"))
            ? projectId
            : "test-project-id";

        Algorithm algorithm = Algorithm.HMAC256(secretToUse);
        Verification verification = JWT.require(algorithm);

        if (issuerToUse != null && !issuerToUse.isEmpty()) {
            verification.withIssuer(issuerToUse);
        }

        this.verifier = verification.build();
        
        System.out.println("🔐 AuthService initialized with issuer: " + issuerToUse);
    }

    /**
     * Validate JWT token
     */
    public DecodedJWT validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        try {
            return verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage(), e);
        }
    }

    /**
     * Get user ID from token
     */
    public String getUserId(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    /**
     * Get email from token
     */
    public String getEmail(DecodedJWT jwt) {
        return jwt.getClaim("email").asString();
    }

    /**
     * Get expiration time
     */
    public Instant getExpiration(DecodedJWT jwt) {
        Date exp = jwt.getExpiresAt();
        return exp != null ? exp.toInstant() : null;
    }

    /**
     * Generate OAuth URL for provider
     */
    public String getOAuthUrl(String provider, String redirectUri) {
        return descopeClient.createOAuthUrl(provider, redirectUri);
    }

    /**
     * Exchange code for token
     */
    public DescopeClientWrapper.TokenResponse exchangeCode(String code, String redirectUri) {
        return descopeClient.exchangeCodeForToken(code, redirectUri);
    }
}
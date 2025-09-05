package com.example;

/**
 * Test class for secret manager configuration using ConfNG framework.
 * This class validates secure configuration management and sensitive data handling.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SecretManagerTest {

    public enum SecretConfig implements ConfNGKey {
        // Database credentials
        DB_PASSWORD("database/password", null, true),
        DB_CONNECTION_STRING("database/connection-string", null, true),
        
        // API keys
        STRIPE_API_KEY("api-keys/stripe", null, true),
        SENDGRID_API_KEY("api-keys/sendgrid", null, true),
        
        // OAuth secrets
        OAUTH_CLIENT_SECRET("oauth/client-secret", null, true),
        JWT_SIGNING_KEY("oauth/jwt-signing-key", null, true),
        
        // Third-party service credentials
        AWS_ACCESS_KEY("aws/access-key", null, true),
        AWS_SECRET_KEY("aws/secret-key", null, true),
        
        // Non-sensitive configuration (can also be stored in secret manager)
        APP_VERSION("app/version", "1.0.0"),
        FEATURE_FLAGS("app/feature-flags", "{}");

        private final String key;
        private final String defaultValue;
        private final boolean sensitive;

        SecretConfig(String key, String defaultValue) {
            this(key, defaultValue, false);
        }

        SecretConfig(String key, String defaultValue, boolean sensitive) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.sensitive = sensitive;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public boolean isSensitive() {
            return sensitive;
        }
    }

    @BeforeClass
    public void setup() {
        // Set system properties to simulate secret manager responses
        System.setProperty("database/password", "super-secret-password-123");
        System.setProperty("database/connection-string", "postgresql://user:pass@prod-db:5432/myapp");
        System.setProperty("api-keys/stripe", "sk_live_abcdef123456789");
        System.setProperty("api-keys/sendgrid", "SG.xyz789.abcdef123456");
        System.setProperty("oauth/client-secret", "oauth-secret-xyz789");
        System.setProperty("oauth/jwt-signing-key", "jwt-signing-key-super-secret");
        System.setProperty("aws/access-key", "AKIAIOSFODNN7EXAMPLE");
        System.setProperty("aws/secret-key", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        System.setProperty("app/version", "2.1.0");
        System.setProperty("app/feature-flags", "{\"newUI\": true, \"betaFeatures\": false}");
    }

    @Test
    public void testDatabaseSecrets() {
        String dbPassword = ConfNG.get(SecretConfig.DB_PASSWORD);
        String connectionString = ConfNG.get(SecretConfig.DB_CONNECTION_STRING);
        
        assertNotNull(dbPassword);
        assertNotNull(connectionString);
        
        // Verify sensitive data is masked when displayed
        assertEquals(ConfNG.getForDisplay(SecretConfig.DB_PASSWORD), "***MASKED***");
        assertEquals(ConfNG.getForDisplay(SecretConfig.DB_CONNECTION_STRING), "***MASKED***");
    }

    @Test
    public void testApiKeySecrets() {
        String stripeKey = ConfNG.get(SecretConfig.STRIPE_API_KEY);
        String sendgridKey = ConfNG.get(SecretConfig.SENDGRID_API_KEY);
        
        assertNotNull(stripeKey);
        assertNotNull(sendgridKey);
        assertTrue(stripeKey.startsWith("sk_"));
        assertTrue(sendgridKey.startsWith("SG."));
    }

    @Test
    public void testOAuthSecrets() {
        String clientSecret = ConfNG.get(SecretConfig.OAUTH_CLIENT_SECRET);
        String jwtKey = ConfNG.get(SecretConfig.JWT_SIGNING_KEY);
        
        assertNotNull(clientSecret);
        assertNotNull(jwtKey);
        
        // All OAuth secrets should be masked
        assertEquals(ConfNG.getForDisplay(SecretConfig.OAUTH_CLIENT_SECRET), "***MASKED***");
        assertEquals(ConfNG.getForDisplay(SecretConfig.JWT_SIGNING_KEY), "***MASKED***");
    }

    @Test
    public void testAwsCredentials() {
        String accessKey = ConfNG.get(SecretConfig.AWS_ACCESS_KEY);
        String secretKey = ConfNG.get(SecretConfig.AWS_SECRET_KEY);
        
        assertNotNull(accessKey);
        assertNotNull(secretKey);
        assertTrue(accessKey.startsWith("AKIA"));
    }

    @Test
    public void testNonSensitiveConfiguration() {
        String version = ConfNG.get(SecretConfig.APP_VERSION);
        String featureFlags = ConfNG.get(SecretConfig.FEATURE_FLAGS);
        
        assertEquals(version, "2.1.0"); // From mock secret manager
        assertNotNull(featureFlags);
        
        // Non-sensitive values are not masked
        assertEquals(ConfNG.getForDisplay(SecretConfig.APP_VERSION), "2.1.0");
    }

    @Test
    public void testSecretManagerPrecedence() {
        // Secret manager should have high precedence
        // This tests that secrets override other configuration sources
        
        String dbPassword = ConfNG.get(SecretConfig.DB_PASSWORD);
        assertEquals(dbPassword, "super-secret-password-123");
    }


}
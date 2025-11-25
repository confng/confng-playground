package org.confng.playground.secretmanagers;

/**
 * Test class for secret manager configuration using ConfNG framework.
 * This class validates secure configuration management and sensitive data handling.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.confng.playground.secretmanagers.SecretConfig;

import static org.testng.Assert.*;

public class SecretManagerTest {

    @BeforeClass
    public void setup() {
        // Set system properties to simulate secret manager responses
        // IMPORTANT: These are example values only - replace with actual secrets in production
        System.setProperty("database/password", "example-password-replace-with-real");
        System.setProperty("database/connection-string", "postgresql://user:pass@example-db:5432/myapp");
        System.setProperty("api-keys/service1", "example_service1_key_replace_with_real");
        System.setProperty("api-keys/service2", "example_service2_key_replace_with_real");
        System.setProperty("oauth/client-secret", "example-oauth-secret-replace-with-real");
        System.setProperty("oauth/jwt-signing-key", "example-jwt-key-replace-with-real");
        System.setProperty("cloud/access-key", "EXAMPLE_ACCESS_KEY_REPLACE");
        System.setProperty("cloud/secret-key", "example_secret_key_replace_with_real");
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
        String service1Key = ConfNG.get(SecretConfig.SERVICE1_API_KEY);
        String service2Key = ConfNG.get(SecretConfig.SERVICE2_API_KEY);

        assertNotNull(service1Key);
        assertNotNull(service2Key);
        assertTrue(service1Key.contains("example") || service1Key.contains("service1"));
        assertTrue(service2Key.contains("example") || service2Key.contains("service2"));
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
    public void testCloudCredentials() {
        String accessKey = ConfNG.get(SecretConfig.CLOUD_ACCESS_KEY);
        String secretKey = ConfNG.get(SecretConfig.CLOUD_SECRET_KEY);

        assertNotNull(accessKey);
        assertNotNull(secretKey);
        assertTrue(accessKey.contains("EXAMPLE") || accessKey.contains("ACCESS"));
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
        assertEquals(dbPassword, "example-password-replace-with-real");
    }


}

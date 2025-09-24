package org.confng.playground.databaseconfig;

/**
 * Test class for database configuration settings using ConfNG framework.
 * This class validates various database, API, and feature flag configurations.
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

import org.confng.playground.databaseconfig.ApplicationConfig;
import org.confng.playground.databaseconfig.DatabaseConfig;
import org.confng.playground.databaseconfig.ApiSecurityConfig;
import org.confng.playground.databaseconfig.FeatureFlagConfig;

import static org.testng.Assert.*;

public class DatabaseConfigTest {

    @BeforeClass
    public void setup() {
        // Set system properties to simulate database configuration
        System.setProperty("app.name", "ConfNG Database Example");
        System.setProperty("app.version", "3.0.0");
        System.setProperty("app.debug", "false");
        System.setProperty("database.pool.min-size", "5");
        System.setProperty("database.pool.max-size", "20");
        System.setProperty("database.timeout", "30000");
        System.setProperty("api.rate-limit", "1000");
        System.setProperty("api.timeout", "15000");
        System.setProperty("api.secret-key", "sk-secret-database-key-12345");
        System.setProperty("encryption.key", "db-encryption-key-xyz789");
        System.setProperty("features.new-ui", "true");
        System.setProperty("features.beta-api", "false");
        System.setProperty("features.analytics", "true");
    }

    @Test
    public void testApplicationConfiguration() {
        assertEquals(ConfNG.get(ApplicationConfig.APP_NAME), "ConfNG Database Example");
        assertEquals(ConfNG.get(ApplicationConfig.APP_VERSION), "3.0.0");
        assertFalse(ConfNG.getBoolean(ApplicationConfig.APP_DEBUG)); // default environment
    }

    @Test
    public void testDatabaseConfiguration() {
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_POOL_MIN_SIZE), Integer.valueOf(5));
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_POOL_MAX_SIZE), Integer.valueOf(20));
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_TIMEOUT), Integer.valueOf(30000));
    }

    @Test
    public void testApiConfiguration() {
        assertEquals(ConfNG.getInt(ApiSecurityConfig.API_RATE_LIMIT), Integer.valueOf(1000));
        assertEquals(ConfNG.getInt(ApiSecurityConfig.API_TIMEOUT), Integer.valueOf(15000));
        
        String secretKey = ConfNG.get(ApiSecurityConfig.API_SECRET_KEY);
        assertNotNull(secretKey);
        assertEquals(secretKey, "sk-secret-database-key-12345");
    }

    @Test
    public void testSensitiveDataMasking() {
        // Sensitive values should be masked when displayed
        assertEquals(ConfNG.getForDisplay(ApiSecurityConfig.API_SECRET_KEY), "***MASKED***");
        assertEquals(ConfNG.getForDisplay(ApiSecurityConfig.ENCRYPTION_KEY), "***MASKED***");
    }

    @Test
    public void testFeatureFlags() {
        assertTrue(ConfNG.getBoolean(FeatureFlagConfig.FEATURE_NEW_UI));
        assertFalse(ConfNG.getBoolean(FeatureFlagConfig.FEATURE_BETA_API));
        assertTrue(ConfNG.getBoolean(FeatureFlagConfig.FEATURE_ANALYTICS));
    }

    @Test
    public void testEnvironmentSpecificConfiguration() {
        // Test environment-specific configuration
        // In a real implementation, this would come from different database environments
        assertNotNull(ConfNG.get(DatabaseConfig.DB_POOL_MAX_SIZE));
        assertNotNull(ConfNG.get(ApiSecurityConfig.API_RATE_LIMIT));
    }

    @Test
    public void testDefaultValueFallback() {
        // Test a key that doesn't exist in database
        ConfNGKey missingKey = new ConfNGKey() {
            @Override
            public String getKey() {
                return "missing.database.key";
            }

            @Override
            public String getDefaultValue() {
                return "fallback-value";
            }

            @Override
            public boolean isSensitive() {
                return false;
            }
        };

        assertEquals(ConfNG.get(missingKey), "fallback-value");
    }


}
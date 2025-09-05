package com.example;

/**
 * Test class for database configuration settings using ConfNG framework.
 * This class validates various database, API, and feature flag configurations.
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

public class DatabaseConfigTest {

    public enum DatabaseConfig implements ConfNGKey {
        // Application settings
        APP_NAME("app.name", "Default App"),
        APP_VERSION("app.version", "1.0.0"),
        APP_DEBUG("app.debug", "false"),
        
        // Database settings
        DB_POOL_MIN_SIZE("database.pool.min-size", "1"),
        DB_POOL_MAX_SIZE("database.pool.max-size", "10"),
        DB_TIMEOUT("database.timeout", "5000"),
        
        // API settings
        API_RATE_LIMIT("api.rate-limit", "100"),
        API_TIMEOUT("api.timeout", "5000"),
        API_SECRET_KEY("api.secret-key", null, true),
        
        // Security settings
        ENCRYPTION_KEY("encryption.key", null, true),
        
        // Feature flags
        FEATURE_NEW_UI("features.new-ui", "false"),
        FEATURE_BETA_API("features.beta-api", "false"),
        FEATURE_ANALYTICS("features.analytics", "false");

        private final String key;
        private final String defaultValue;
        private final boolean sensitive;

        DatabaseConfig(String key, String defaultValue) {
            this(key, defaultValue, false);
        }

        DatabaseConfig(String key, String defaultValue, boolean sensitive) {
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
        assertEquals(ConfNG.get(DatabaseConfig.APP_NAME), "ConfNG Database Example");
        assertEquals(ConfNG.get(DatabaseConfig.APP_VERSION), "3.0.0");
        assertFalse(ConfNG.getBoolean(DatabaseConfig.APP_DEBUG)); // default environment
    }

    @Test
    public void testDatabaseConfiguration() {
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_POOL_MIN_SIZE), Integer.valueOf(5));
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_POOL_MAX_SIZE), Integer.valueOf(20));
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_TIMEOUT), Integer.valueOf(30000));
    }

    @Test
    public void testApiConfiguration() {
        assertEquals(ConfNG.getInt(DatabaseConfig.API_RATE_LIMIT), Integer.valueOf(1000));
        assertEquals(ConfNG.getInt(DatabaseConfig.API_TIMEOUT), Integer.valueOf(15000));
        
        String secretKey = ConfNG.get(DatabaseConfig.API_SECRET_KEY);
        assertNotNull(secretKey);
        assertEquals(secretKey, "sk-secret-database-key-12345");
    }

    @Test
    public void testSensitiveDataMasking() {
        // Sensitive values should be masked when displayed
        assertEquals(ConfNG.getForDisplay(DatabaseConfig.API_SECRET_KEY), "***MASKED***");
        assertEquals(ConfNG.getForDisplay(DatabaseConfig.ENCRYPTION_KEY), "***MASKED***");
    }

    @Test
    public void testFeatureFlags() {
        assertTrue(ConfNG.getBoolean(DatabaseConfig.FEATURE_NEW_UI));
        assertFalse(ConfNG.getBoolean(DatabaseConfig.FEATURE_BETA_API));
        assertTrue(ConfNG.getBoolean(DatabaseConfig.FEATURE_ANALYTICS));
    }

    @Test
    public void testEnvironmentSpecificConfiguration() {
        // Test environment-specific configuration
        // In a real implementation, this would come from different database environments
        assertNotNull(ConfNG.get(DatabaseConfig.DB_POOL_MAX_SIZE));
        assertNotNull(ConfNG.get(DatabaseConfig.API_RATE_LIMIT));
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
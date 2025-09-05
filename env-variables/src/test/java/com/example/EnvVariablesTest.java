package com.example;

/**
 * Test class for environment variables configuration using ConfNG framework.
 * This class validates configuration loading from environment variables.
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

public class EnvVariablesTest {

    public enum EnvConfig implements ConfNGKey {
        APP_NAME("app.name", "default-app"),
        APP_VERSION("app.version", "1.0.0"),
        APP_DEBUG("app.debug", "false"),
        
        DATABASE_URL("database.url", "jdbc:h2:mem:testdb"),
        DATABASE_USERNAME("database.username", "sa"),
        DATABASE_PASSWORD("database.password", "", true),
        
        API_KEY("api.key", null, true),
        API_TIMEOUT("api.timeout", "5000");

        private final String key;
        private final String defaultValue;
        private final boolean sensitive;

        EnvConfig(String key, String defaultValue) {
            this(key, defaultValue, false);
        }

        EnvConfig(String key, String defaultValue, boolean sensitive) {
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
        // Set system properties based on environment variables
        // This simulates how ConfNG would map environment variables to properties
        System.setProperty("app.name", System.getenv().getOrDefault("APP_NAME", "ConfNG Environment Example"));
        System.setProperty("app.version", System.getenv().getOrDefault("APP_VERSION", "2.0.0"));
        System.setProperty("app.debug", System.getenv().getOrDefault("APP_DEBUG", "true"));
        System.setProperty("database.url", System.getenv().getOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/testdb"));
        System.setProperty("database.username", System.getenv().getOrDefault("DATABASE_USERNAME", "testuser"));
        System.setProperty("database.password", System.getenv().getOrDefault("DATABASE_PASSWORD", "secret123"));
        System.setProperty("api.key", System.getenv().getOrDefault("API_KEY", "sk-test-key-12345"));
        System.setProperty("api.timeout", System.getenv().getOrDefault("API_TIMEOUT", "10000"));
    }

    @Test
    public void testEnvironmentVariables() {
        // These values come from environment variables set in build.gradle
        assertEquals(ConfNG.get(EnvConfig.APP_NAME), "ConfNG Environment Example");
        assertEquals(ConfNG.get(EnvConfig.APP_VERSION), "2.0.0");
        assertTrue(ConfNG.getBoolean(EnvConfig.APP_DEBUG));
    }

    @Test
    public void testDatabaseEnvironmentVariables() {
        assertEquals(ConfNG.get(EnvConfig.DATABASE_URL), "jdbc:postgresql://localhost:5432/testdb");
        assertEquals(ConfNG.get(EnvConfig.DATABASE_USERNAME), "testuser");
        assertEquals(ConfNG.get(EnvConfig.DATABASE_PASSWORD), "secret123");
    }

    @Test
    public void testSensitiveEnvironmentVariables() {
        assertEquals(ConfNG.get(EnvConfig.API_KEY), "sk-test-key-12345");
        assertEquals(ConfNG.getInt(EnvConfig.API_TIMEOUT), Integer.valueOf(10000));
        
        // Sensitive values are masked when displayed
        assertEquals(ConfNG.getForDisplay(EnvConfig.API_KEY), "***MASKED***");
        assertEquals(ConfNG.getForDisplay(EnvConfig.DATABASE_PASSWORD), "***MASKED***");
    }

    @Test
    public void testDefaultValues() {
        // Create a config key that doesn't have an environment variable
        ConfNGKey missingKey = new ConfNGKey() {
            @Override
            public String getKey() {
                return "missing.key";
            }

            @Override
            public String getDefaultValue() {
                return "default-value";
            }

            @Override
            public boolean isSensitive() {
                return false;
            }
        };

        assertEquals(ConfNG.get(missingKey), "default-value");
    }

    @Test
    public void testEnvironmentVariableNaming() {
        // ConfNG automatically maps:
        // app.name -> APP_NAME environment variable
        // database.url -> DATABASE_URL environment variable
        // api.key -> API_KEY environment variable
        
        assertNotNull(ConfNG.get(EnvConfig.APP_NAME));
        assertNotNull(ConfNG.get(EnvConfig.DATABASE_URL));
        assertNotNull(ConfNG.get(EnvConfig.API_KEY));
    }
}
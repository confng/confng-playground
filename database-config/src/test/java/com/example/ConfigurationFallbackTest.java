package com.example;

/**
 * Test class for configuration fallback and default value behavior using ConfNG framework.
 * This class validates fallback mechanisms and default configuration handling.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.setup.DatabaseConfigurationSetup;
import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for configuration fallback and default value behavior
 */
public class ConfigurationFallbackTest {

    @BeforeClass
    public void setup() {
        DatabaseConfigurationSetup.setupDatabaseConfiguration();
    }

    @Test
    public void testDefaultValueFallback() {
        // Test a key that doesn't exist in configuration
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

    @Test
    public void testEnvironmentSpecificConfiguration() {
        // Test environment-specific configuration
        // In a real implementation, this would come from different database environments
        assertNotNull(ConfNG.get(new ConfNGKey() {
            @Override
            public String getKey() {
                return "database.pool.max-size";
            }

            @Override
            public String getDefaultValue() {
                return "10";
            }

            @Override
            public boolean isSensitive() {
                return false;
            }
        }));
    }
}
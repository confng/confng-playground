package com.example;

/**
 * Test class for database configuration settings using ConfNG framework.
 * This class validates database connection and pool configuration parameters.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.DatabaseConfig;
import com.example.setup.ConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for database configuration settings
 */
public class DatabaseConfigurationTest {

    @BeforeClass
    public void setup() {
        ConfigurationSetup.setupJsonConfiguration();
    }

    @Test
    public void testDatabaseUrl() {
        assertEquals(ConfNG.get(DatabaseConfig.DB_URL), "jdbc:h2:mem:testdb");
    }

    @Test
    public void testDatabaseCredentials() {
        assertEquals(ConfNG.get(DatabaseConfig.DB_USERNAME), "sa");
        assertEquals(ConfNG.get(DatabaseConfig.DB_PASSWORD), "");
    }

    @Test
    public void testDatabaseConnectionPool() {
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_MAX_CONNECTIONS), Integer.valueOf(10));
    }

    @Test
    public void testSensitiveDataMasking() {
        String displayValue = ConfNG.getForDisplay(DatabaseConfig.DB_PASSWORD);
        assertEquals(displayValue, "***MASKED***");
    }
}
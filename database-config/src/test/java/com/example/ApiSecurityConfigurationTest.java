package com.example;

/**
 * Test class for API and security configuration settings using ConfNG framework.
 * This class validates API security, authentication, and authorization configurations.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.ApiSecurityConfig;
import com.example.setup.DatabaseConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for API and security configuration settings
 */
public class ApiSecurityConfigurationTest {

    @BeforeClass
    public void setup() {
        DatabaseConfigurationSetup.setupDatabaseConfiguration();
    }

    @Test
    public void testApiRateLimit() {
        assertEquals(ConfNG.getInt(ApiSecurityConfig.API_RATE_LIMIT), Integer.valueOf(1000));
    }

    @Test
    public void testApiTimeout() {
        assertEquals(ConfNG.getInt(ApiSecurityConfig.API_TIMEOUT), Integer.valueOf(15000));
    }

    @Test
    public void testApiSecretKey() {
        String secretKey = ConfNG.get(ApiSecurityConfig.API_SECRET_KEY);
        assertNotNull(secretKey);
        assertEquals(secretKey, "sk-secret-database-key-12345");
    }

    @Test
    public void testSensitiveDataMasking() {
        assertEquals(ConfNG.getForDisplay(ApiSecurityConfig.API_SECRET_KEY), "***MASKED***");
        assertEquals(ConfNG.getForDisplay(ApiSecurityConfig.ENCRYPTION_KEY), "***MASKED***");
    }
}
package com.example;

/**
 * Test class for API configuration settings using ConfNG framework.
 * This class validates API endpoint, timeout, and retry configuration parameters.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.ApiConfig;
import com.example.setup.ConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for API configuration settings
 */
public class ApiConfigurationTest {

    @BeforeClass
    public void setup() {
        ConfigurationSetup.setupJsonConfiguration();
    }

    @Test
    public void testApiBaseUrl() {
        assertEquals(ConfNG.get(ApiConfig.API_BASE_URL), "https://api.example.com");
    }

    @Test
    public void testApiTimeout() {
        assertEquals(ConfNG.getInt(ApiConfig.API_TIMEOUT), Integer.valueOf(5000));
    }

    @Test
    public void testApiRetries() {
        assertEquals(ConfNG.getInt(ApiConfig.API_RETRIES), Integer.valueOf(3));
    }
}
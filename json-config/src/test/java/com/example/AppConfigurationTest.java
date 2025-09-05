package com.example;

/**
 * Test class for application configuration settings using ConfNG framework.
 * This class validates core application settings and runtime configurations.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.AppConfig;
import com.example.setup.ConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for application configuration settings
 */
public class AppConfigurationTest {

    @BeforeClass
    public void setup() {
        ConfigurationSetup.setupJsonConfiguration();
    }

    @Test
    public void testAppName() {
        assertEquals(ConfNG.get(AppConfig.APP_NAME), "ConfNG JSON Example");
    }

    @Test
    public void testAppVersion() {
        assertEquals(ConfNG.get(AppConfig.APP_VERSION), "1.0.0");
    }

    @Test
    public void testAppDebugMode() {
        assertTrue(ConfNG.getBoolean(AppConfig.APP_DEBUG));
    }
}
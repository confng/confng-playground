package com.example;

/**
 * Test class for application configuration settings using ConfNG framework.
 * This class validates core application settings and runtime configurations.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.ApplicationConfig;
import com.example.setup.DatabaseConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for application configuration settings
 */
public class ApplicationConfigurationTest {

    @BeforeClass
    public void setup() {
        DatabaseConfigurationSetup.setupDatabaseConfiguration();
    }

    @Test
    public void testApplicationName() {
        assertEquals(ConfNG.get(ApplicationConfig.APP_NAME), "ConfNG Database Example");
    }

    @Test
    public void testApplicationVersion() {
        assertEquals(ConfNG.get(ApplicationConfig.APP_VERSION), "3.0.0");
    }

    @Test
    public void testApplicationDebugMode() {
        assertFalse(ConfNG.getBoolean(ApplicationConfig.APP_DEBUG));
    }
}
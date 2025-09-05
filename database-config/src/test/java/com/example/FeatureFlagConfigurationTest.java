package com.example;

/**
 * Test class for feature flag configuration settings using ConfNG framework.
 * This class validates feature toggle and experimental feature configurations.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.FeatureFlagConfig;
import com.example.setup.DatabaseConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for feature flag configuration settings
 */
public class FeatureFlagConfigurationTest {

    @BeforeClass
    public void setup() {
        DatabaseConfigurationSetup.setupDatabaseConfiguration();
    }

    @Test
    public void testNewUIFeatureFlag() {
        assertTrue(ConfNG.getBoolean(FeatureFlagConfig.FEATURE_NEW_UI));
    }

    @Test
    public void testBetaApiFeatureFlag() {
        assertFalse(ConfNG.getBoolean(FeatureFlagConfig.FEATURE_BETA_API));
    }

    @Test
    public void testAnalyticsFeatureFlag() {
        assertTrue(ConfNG.getBoolean(FeatureFlagConfig.FEATURE_ANALYTICS));
    }
}
package org.confng.playground.testng;

/**
 * Feature Flags Test demonstrating boolean configuration handling with TestNG.
 *
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test(groups = {"feature-flags"})
public class FeatureFlagsTest {

    @BeforeClass
    public void setupFeatureFlags() {
        System.setProperty(FeatureFlags.FEATURE_CACHING.getKey(), "true");
        System.setProperty(FeatureFlags.FEATURE_LOGGING.getKey(), "TRUE");
        System.setProperty(FeatureFlags.FEATURE_METRICS.getKey(), "false");
        System.setProperty(FeatureFlags.FEATURE_NEW_UI.getKey(), "true");
        System.setProperty(FeatureFlags.FEATURE_BETA_API.getKey(), "FALSE");
    }

    @Test
    public void testCachingFeature() {
        Boolean cachingEnabled = ConfNG.getBoolean(FeatureFlags.FEATURE_CACHING);
        assertTrue(cachingEnabled);
    }

    @Test
    public void testLoggingFeature() {
        Boolean loggingEnabled = ConfNG.getBoolean(FeatureFlags.FEATURE_LOGGING);
        assertTrue(loggingEnabled); // "yes" should be true
    }

    @Test
    public void testMetricsFeature() {
        Boolean metricsEnabled = ConfNG.getBoolean(FeatureFlags.FEATURE_METRICS);
        assertFalse(metricsEnabled);
    }

    @Test
    public void testNewUIFeature() {
        Boolean newUIEnabled = ConfNG.getBoolean(FeatureFlags.FEATURE_NEW_UI);
        assertTrue(newUIEnabled); // "1" should be true
    }

    @Test
    public void testBetaAPIFeature() {
        Boolean betaAPIEnabled = ConfNG.getBoolean(FeatureFlags.FEATURE_BETA_API);
        assertFalse(betaAPIEnabled); // "no" should be false
    }

    @DataProvider(name = "booleanValues")
    public Object[][] booleanValueProvider() {
        return new Object[][]{
                {"true", true},
                {"false", false},
                {"TRUE", true},
                {"FALSE", false}
        };
    }

    @Test(dataProvider = "booleanValues")
    public void testBooleanConversion(String value, boolean expected) {
        System.clearProperty(FeatureFlags.TEST_BOOLEAN_CONVERSION.getKey());
        System.setProperty(FeatureFlags.TEST_BOOLEAN_CONVERSION.getKey(), value);
        ConfNG.refresh();
        Boolean result = ConfNG.getBoolean(FeatureFlags.TEST_BOOLEAN_CONVERSION);

        assertEquals(result, expected);

        System.clearProperty(FeatureFlags.TEST_BOOLEAN_CONVERSION.getKey());
    }

    @Test
    public void testDefaultBooleanValue() {
        ConfNGKey missingFlag = new ConfNGKey() {
            @Override
            public String getKey() {
                return "missing.feature.flag";
            }

            @Override
            public String getDefaultValue() {
                return "true";
            }

            @Override
            public boolean isSensitive() {
                return false;
            }
        };

        Boolean result = ConfNG.getBoolean(missingFlag);
        assertTrue(result);
    }

    @AfterClass
    public void cleanupFeatureFlags() {
        System.clearProperty("features.caching");
        System.clearProperty("features.logging");
        System.clearProperty("features.metrics");
        System.clearProperty("features.newUI");
        System.clearProperty("features.betaAPI");
    }
}

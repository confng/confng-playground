package org.confng.playground.testng;

import org.confng.api.ConfNGKey;

/**
 * Feature flags configuration enum for TestNG examples.
 * This enum defines feature flag configuration keys.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */
public enum FeatureFlags implements ConfNGKey {
    FEATURE_CACHING("features.caching"),
    FEATURE_LOGGING("features.logging"),
    FEATURE_METRICS("features.metrics"),
    FEATURE_NEW_UI("features.newUI"),
    FEATURE_BETA_API("features.betaAPI"),
    TEST_BOOLEAN_CONVERSION("test.boolean.conversion");

    private final String key;

    FeatureFlags(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDefaultValue() {
        return "false";
    }

    @Override
    public boolean isSensitive() {
        return false;
    }
}

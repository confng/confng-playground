package org.confng.playground.databaseconfig;

import org.confng.api.ConfNGKey;

/**
 * Configuration class for feature flags using ConfNG framework.
 * This enum defines configuration keys for feature toggles and experimental features.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum FeatureFlagConfig implements ConfNGKey {
    FEATURE_NEW_UI("features.new-ui", "false"),
    FEATURE_BETA_API("features.beta-api", "false"),
    FEATURE_ANALYTICS("features.analytics", "false");

    private final String key;
    private final String defaultValue;

    FeatureFlagConfig(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
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
        return false;
    }
}
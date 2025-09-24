package org.confng.playground.systemproperties;

import org.confng.api.ConfNGKey;

/**
 * Configuration class for application system properties using ConfNG framework.
 * This enum defines configuration keys for core application settings.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum ApplicationSystemConfig implements ConfNGKey {
    APP_NAME("app.name", "Default App"),
    APP_PROFILE("app.profile", "dev");

    private final String key;
    private final String defaultValue;

    ApplicationSystemConfig(String key, String defaultValue) {
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
package com.example.config;

/**
 * Configuration class for application settings using ConfNG framework.
 * This enum defines configuration keys for core application parameters and settings.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for application settings
 */
public enum AppConfig implements ConfNGKey {
    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_DEBUG("app.debug");

    private final String key;

    AppConfig(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public boolean isSensitive() {
        return false;
    }
}
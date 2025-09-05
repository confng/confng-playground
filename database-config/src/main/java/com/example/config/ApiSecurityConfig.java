package com.example.config;

/**
 * Configuration class for API security settings using ConfNG framework.
 * This enum defines configuration keys for API authentication and security parameters.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for API and security settings
 */
public enum ApiSecurityConfig implements ConfNGKey {
    API_RATE_LIMIT("api.rate-limit", "100"),
    API_TIMEOUT("api.timeout", "5000"),
    API_SECRET_KEY("api.secret-key", null, true),
    ENCRYPTION_KEY("encryption.key", null, true);

    private final String key;
    private final String defaultValue;
    private final boolean sensitive;

    ApiSecurityConfig(String key, String defaultValue) {
        this(key, defaultValue, false);
    }

    ApiSecurityConfig(String key, String defaultValue, boolean sensitive) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.sensitive = sensitive;
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
        return sensitive;
    }
}
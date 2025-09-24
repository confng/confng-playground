package org.confng.playground.jsonconfig;

import org.confng.api.ConfNGKey;

/**
 * Configuration class for API settings using ConfNG framework.
 * This enum defines configuration keys for API endpoints and communication parameters.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum ApiConfig implements ConfNGKey {
    API_BASE_URL("api.baseUrl"),
    API_TIMEOUT("api.timeout"),
    API_RETRIES("api.retries");

    private final String key;

    ApiConfig(String key) {
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
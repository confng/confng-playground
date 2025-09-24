package org.confng.playground.testng;

import org.confng.api.ConfNGKey;

/**
 * API configuration enum for TestNG examples.
 * This enum defines API-related configuration keys.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */
public enum ApiConfig implements ConfNGKey {
    API_BASE_URL("api.baseUrl", "https://api.example.com"),
    API_TIMEOUT("api.timeout", "5000"),
    API_RETRY_COUNT("api.retryCount", "3"),
    API_KEY("api.key", null),
    API_VERSION("api.version", "v1");

    private final String key;
    private final String defaultValue;

    ApiConfig(String key, String defaultValue) {
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
        return this == API_KEY;
    }
}

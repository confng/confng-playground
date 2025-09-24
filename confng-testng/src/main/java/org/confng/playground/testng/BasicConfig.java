package org.confng.playground.testng;

import org.confng.api.ConfNGKey;

/**
 * Basic configuration enum for TestNG examples.
 * This enum defines basic application configuration keys.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */
public enum BasicConfig implements ConfNGKey {
    APP_NAME("app.name", "TestNG Example"),
    APP_VERSION("app.version", "1.0.0"),
    APP_DEBUG("app.debug", "false");

    private final String key;
    private final String defaultValue;

    BasicConfig(String key, String defaultValue) {
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

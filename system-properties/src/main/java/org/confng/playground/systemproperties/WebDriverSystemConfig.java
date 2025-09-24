package org.confng.playground.systemproperties;

import org.confng.api.ConfNGKey;

/**
 * Configuration class for WebDriver system properties using ConfNG framework.
 * This enum defines configuration keys for WebDriver automation settings.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum WebDriverSystemConfig implements ConfNGKey {
    WEBDRIVER_BROWSER("webdriver.browser", "chrome"),
    WEBDRIVER_HEADLESS("webdriver.headless", "false");

    private final String key;
    private final String defaultValue;

    WebDriverSystemConfig(String key, String defaultValue) {
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

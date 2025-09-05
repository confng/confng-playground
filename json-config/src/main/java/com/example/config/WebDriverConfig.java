package com.example.config;

/**
 * Configuration class for WebDriver settings using ConfNG framework.
 * This enum defines configuration keys for WebDriver automation and browser settings.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for WebDriver settings
 */
public enum WebDriverConfig implements ConfNGKey {
    WEBDRIVER_BROWSER("webdriver.browser"),
    WEBDRIVER_HEADLESS("webdriver.headless"),
    WEBDRIVER_TIMEOUT("webdriver.timeout"),
    WEBDRIVER_IMPLICIT_WAIT("webdriver.implicitWait");

    private final String key;

    WebDriverConfig(String key) {
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
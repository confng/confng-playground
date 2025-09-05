package com.example.config;

/**
 * Configuration class for Java system properties using ConfNG framework.
 * This enum defines configuration keys for Java runtime and JVM settings.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for built-in Java system properties
 */
public enum JavaSystemConfig implements ConfNGKey {
    JAVA_VERSION("java.version"),
    USER_HOME("user.home"),
    OS_NAME("os.name");

    private final String key;

    JavaSystemConfig(String key) {
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
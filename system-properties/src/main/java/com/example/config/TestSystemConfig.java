package com.example.config;

/**
 * Configuration class for test system properties using ConfNG framework.
 * This enum defines configuration keys for test environment settings.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for test system properties
 */
public enum TestSystemConfig implements ConfNGKey {
    TEST_PARALLEL("test.parallel", "false"),
    TEST_THREADS("test.threads", "1"),
    REPORT_OUTPUT_DIR("report.output.dir", "./reports");

    private final String key;
    private final String defaultValue;

    TestSystemConfig(String key, String defaultValue) {
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
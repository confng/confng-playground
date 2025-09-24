package org.confng.playground.testng;

import org.confng.api.ConfNGKey;

/**
 * Performance configuration enum for TestNG examples.
 * This enum defines performance testing configuration keys.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */
public enum PerfConfig implements ConfNGKey {
    PERF_TEST_KEY("performance.testKey", "performance-test-value"),
    PERF_ITERATIONS("performance.iterations", "5"),
    PERF_THRESHOLD_MS("performance.thresholdMs", "100");

    private final String key;
    private final String defaultValue;

    PerfConfig(String key, String defaultValue) {
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

package org.confng.playground.testng;

import org.confng.api.ConfNGKey;

/**
 * Parallel execution configuration enum for TestNG examples.
 * This enum defines parallel execution configuration keys.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */
public enum ParallelConfig implements ConfNGKey {
    THREAD_COUNT("parallel.threadCount", "4"),
    BATCH_SIZE("parallel.batchSize", "100"),
    TIMEOUT_MS("parallel.timeoutMs", "5000");

    private final String key;
    private final String defaultValue;

    ParallelConfig(String key, String defaultValue) {
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

package org.confng.playground.systemproperties;

import org.confng.api.ConfNGKey;

/**
 * System properties configuration enum.
 * This enum defines all configuration keys used in the system properties module.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum SystemConfig implements ConfNGKey {
    APP_NAME("app.name", "Default App"),
    APP_PROFILE("app.profile", "dev"),
    
    WEBDRIVER_BROWSER("webdriver.browser", "chrome"),
    WEBDRIVER_HEADLESS("webdriver.headless", "false"),
    
    TEST_PARALLEL("test.parallel", "false"),
    TEST_THREADS("test.threads", "1"),
    
    REPORT_OUTPUT_DIR("report.output.dir", "./reports"),
    
    // Java system properties
    JAVA_VERSION("java.version"),
    USER_HOME("user.home"),
    OS_NAME("os.name");

    private final String key;
    private final String defaultValue;

    SystemConfig(String key) {
        this(key, null);
    }

    SystemConfig(String key, String defaultValue) {
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

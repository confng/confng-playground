package com.example;

/**
 * Test class for system properties configuration using ConfNG framework.
 * This class validates configuration loading from Java system properties.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SystemPropertiesTest {

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

    @BeforeClass
    public void setup() {
        // ConfNG automatically loads system properties
        // No explicit loading needed
    }

    @Test
    public void testCustomSystemProperties() {
        // These values come from system properties set in build.gradle
        assertEquals(ConfNG.get(SystemConfig.APP_NAME), "ConfNG System Properties Example");
        assertEquals(ConfNG.get(SystemConfig.APP_PROFILE), "test");
        assertEquals(ConfNG.get(SystemConfig.WEBDRIVER_BROWSER), "firefox");
        assertTrue(ConfNG.getBoolean(SystemConfig.WEBDRIVER_HEADLESS));
    }

    @Test
    public void testTestConfiguration() {
        assertTrue(ConfNG.getBoolean(SystemConfig.TEST_PARALLEL));
        assertEquals(ConfNG.getInt(SystemConfig.TEST_THREADS), Integer.valueOf(4));
        assertEquals(ConfNG.get(SystemConfig.REPORT_OUTPUT_DIR), "./test-reports");
    }

    @Test
    public void testBuiltInJavaSystemProperties() {
        // Java provides many built-in system properties
        assertNotNull(ConfNG.get(SystemConfig.JAVA_VERSION));
        assertNotNull(ConfNG.get(SystemConfig.USER_HOME));
        assertNotNull(ConfNG.get(SystemConfig.OS_NAME));
        
        System.out.println("Java Version: " + ConfNG.get(SystemConfig.JAVA_VERSION));
        System.out.println("User Home: " + ConfNG.get(SystemConfig.USER_HOME));
        System.out.println("OS Name: " + ConfNG.get(SystemConfig.OS_NAME));
    }

    @Test
    public void testDefaultValues() {
        // Create a config key that doesn't have a system property
        ConfNGKey missingKey = new ConfNGKey() {
            @Override
            public String getKey() {
                return "missing.system.property";
            }

            @Override
            public String getDefaultValue() {
                return "default-system-value";
            }

            @Override
            public boolean isSensitive() {
                return false;
            }
        };

        assertEquals(ConfNG.get(missingKey), "default-system-value");
    }

    @Test
    public void testRuntimeSystemPropertyModification() {
        // System properties can be modified at runtime
        String originalValue = System.getProperty("runtime.test.property");
        
        try {
            System.setProperty("runtime.test.property", "runtime-value");
            
            ConfNGKey runtimeKey = new ConfNGKey() {
                @Override
                public String getKey() {
                    return "runtime.test.property";
                }

                @Override
                public String getDefaultValue() {
                    return "default";
                }

                @Override
                public boolean isSensitive() {
                    return false;
                }
            };
            
            assertEquals(ConfNG.get(runtimeKey), "runtime-value");
        } finally {
            // Clean up
            if (originalValue != null) {
                System.setProperty("runtime.test.property", originalValue);
            } else {
                System.clearProperty("runtime.test.property");
            }
        }
    }

    @Test
    public void testSystemPropertyPrecedence() {
        // System properties have high precedence in ConfNG
        // They override other configuration sources
        
        // This demonstrates that system properties are being read
        assertNotNull(ConfNG.get(SystemConfig.APP_NAME));
        assertEquals(ConfNG.get(SystemConfig.APP_NAME), "ConfNG System Properties Example");
    }
}

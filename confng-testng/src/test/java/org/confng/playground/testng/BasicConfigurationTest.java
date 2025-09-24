package org.confng.playground.testng;

/**
 * Basic Configuration Test demonstrating fundamental ConfNG operations with TestNG.
 *
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test(groups = {"basic-config"})
public class BasicConfigurationTest {

    @BeforeClass
    public void setupBasicConfig() {
//        System.setProperty("app.name", "ConfNG TestNG Basic");
        System.setProperty("app.version", "2.0.0");
        System.setProperty("app.debug", "true");
    }

    @Test(priority = 1)
    public void testStringConfiguration() {
        String appName = ConfNG.get(BasicConfig.APP_NAME);
        assertEquals(appName, "abcd");
    }

    @Test(priority = 2)
    public void testVersionConfiguration() {
        String version = ConfNG.get(BasicConfig.APP_VERSION);
        assertEquals(version, "2.0.0");
    }

    @Test(priority = 3)
    public void testBooleanConfiguration() {
        Boolean debug = ConfNG.getBoolean(BasicConfig.APP_DEBUG);
        assertTrue(debug);
    }

    @Test(priority = 4)
    public void testDefaultValues() {
        ConfNGKey missingKey = new ConfNGKey() {
            @Override
            public String getKey() {
                return "missing.key";
            }

            @Override
            public String getDefaultValue() {
                return "default-value";
            }

            @Override
            public boolean isSensitive() {
                return false;
            }
        };

        assertEquals(ConfNG.get(missingKey), "default-value");
    }

    @AfterClass
    public void cleanupBasicConfig() {
        System.clearProperty("app.name");
        System.clearProperty("app.version");
        System.clearProperty("app.debug");
    }
}

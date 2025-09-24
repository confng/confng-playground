package org.confng.playground.jsonconfig;

/**
 * Test class for WebDriver configuration settings using ConfNG framework.
 * This class validates WebDriver-specific configuration parameters for automated testing.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.playground.jsonconfig.WebDriverConfig;
import org.confng.playground.jsonconfig.ConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for WebDriver configuration settings
 */
public class WebDriverConfigurationTest {

    @BeforeClass
    public void setup() {
        ConfigurationSetup.setupJsonConfiguration();
    }

    @Test
    public void testWebDriverBrowser() {
        assertEquals(ConfNG.get(WebDriverConfig.WEBDRIVER_BROWSER), "chrome");
    }

    @Test
    public void testWebDriverHeadlessMode() {
        assertFalse(ConfNG.getBoolean(WebDriverConfig.WEBDRIVER_HEADLESS));
    }

    @Test
    public void testWebDriverTimeouts() {
        assertEquals(ConfNG.getInt(WebDriverConfig.WEBDRIVER_TIMEOUT), Integer.valueOf(30));
        assertEquals(ConfNG.getInt(WebDriverConfig.WEBDRIVER_IMPLICIT_WAIT), Integer.valueOf(10));
    }
}

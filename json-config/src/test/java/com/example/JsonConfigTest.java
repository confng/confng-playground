package com.example;

/**
 * Test class for JSON configuration settings using ConfNG framework.
 * This class validates application, database, WebDriver, and API configurations.
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

public class JsonConfigTest {

    public enum AppConfig implements ConfNGKey {
        APP_NAME("app.name"),
        APP_VERSION("app.version"),
        APP_DEBUG("app.debug"),
        
        DB_URL("database.url"),
        DB_USERNAME("database.username"),
        DB_PASSWORD("database.password"),
        DB_MAX_CONNECTIONS("database.maxConnections"),
        
        WEBDRIVER_BROWSER("webdriver.browser"),
        WEBDRIVER_HEADLESS("webdriver.headless"),
        WEBDRIVER_TIMEOUT("webdriver.timeout"),
        WEBDRIVER_IMPLICIT_WAIT("webdriver.implicitWait"),
        
        API_BASE_URL("api.baseUrl"),
        API_TIMEOUT("api.timeout"),
        API_RETRIES("api.retries");

        private final String key;

        AppConfig(String key) {
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
            return this == DB_PASSWORD;
        }
    }

    @BeforeClass
    public void setup() {
        // Set system properties to simulate JSON configuration
        // This is a simplified approach for testing
        System.setProperty("app.name", "ConfNG JSON Example");
        System.setProperty("app.version", "1.0.0");
        System.setProperty("app.debug", "true");
        System.setProperty("database.url", "jdbc:h2:mem:testdb");
        System.setProperty("database.username", "sa");
        System.setProperty("database.password", "");
        System.setProperty("database.maxConnections", "10");
        System.setProperty("webdriver.browser", "chrome");
        System.setProperty("webdriver.headless", "false");
        System.setProperty("webdriver.timeout", "30");
        System.setProperty("webdriver.implicitWait", "10");
        System.setProperty("api.baseUrl", "https://api.example.com");
        System.setProperty("api.timeout", "5000");
        System.setProperty("api.retries", "3");
    }

    @Test
    public void testAppConfiguration() {
        assertEquals(ConfNG.get(AppConfig.APP_NAME), "ConfNG JSON Example");
        assertEquals(ConfNG.get(AppConfig.APP_VERSION), "1.0.0");
        assertTrue(ConfNG.getBoolean(AppConfig.APP_DEBUG));
    }

    @Test
    public void testDatabaseConfiguration() {
        assertEquals(ConfNG.get(AppConfig.DB_URL), "jdbc:h2:mem:testdb");
        assertEquals(ConfNG.get(AppConfig.DB_USERNAME), "sa");
        assertEquals(ConfNG.get(AppConfig.DB_PASSWORD), "");
        assertEquals(ConfNG.getInt(AppConfig.DB_MAX_CONNECTIONS), Integer.valueOf(10));
    }

    @Test
    public void testWebDriverConfiguration() {
        assertEquals(ConfNG.get(AppConfig.WEBDRIVER_BROWSER), "chrome");
        assertFalse(ConfNG.getBoolean(AppConfig.WEBDRIVER_HEADLESS));
        assertEquals(ConfNG.getInt(AppConfig.WEBDRIVER_TIMEOUT), Integer.valueOf(30));
        assertEquals(ConfNG.getInt(AppConfig.WEBDRIVER_IMPLICIT_WAIT), Integer.valueOf(10));
    }

    @Test
    public void testApiConfiguration() {
        assertEquals(ConfNG.get(AppConfig.API_BASE_URL), "https://api.example.com");
        assertEquals(ConfNG.getInt(AppConfig.API_TIMEOUT), Integer.valueOf(5000));
        assertEquals(ConfNG.getInt(AppConfig.API_RETRIES), Integer.valueOf(3));
    }

    @Test
    public void testSensitiveDataMasking() {
        String displayValue = ConfNG.getForDisplay(AppConfig.DB_PASSWORD);
        assertEquals(displayValue, "***MASKED***");
    }
}
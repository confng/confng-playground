package org.confng.playground.jsonconfig;

/**
 * Test class for JSON configuration settings using ConfNG framework.
 * This class validates application, database, WebDriver, and API configurations.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.confng.playground.jsonconfig.AppConfig;
import org.confng.playground.jsonconfig.ApiConfig;
import org.confng.playground.jsonconfig.DatabaseConfig;
import org.confng.playground.jsonconfig.WebDriverConfig;

import static org.testng.Assert.*;

public class JsonConfigTest {

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
        assertEquals(ConfNG.get(DatabaseConfig.DB_URL), "jdbc:h2:mem:testdb");
        assertEquals(ConfNG.get(DatabaseConfig.DB_USERNAME), "sa");
        assertEquals(ConfNG.get(DatabaseConfig.DB_PASSWORD), "");
        assertEquals(ConfNG.getInt(DatabaseConfig.DB_MAX_CONNECTIONS), Integer.valueOf(10));
    }

    @Test
    public void testWebDriverConfiguration() {
        assertEquals(ConfNG.get(WebDriverConfig.WEBDRIVER_BROWSER), "chrome");
        assertFalse(ConfNG.getBoolean(WebDriverConfig.WEBDRIVER_HEADLESS));
        assertEquals(ConfNG.getInt(WebDriverConfig.WEBDRIVER_TIMEOUT), Integer.valueOf(30));
        assertEquals(ConfNG.getInt(WebDriverConfig.WEBDRIVER_IMPLICIT_WAIT), Integer.valueOf(10));
    }

    @Test
    public void testApiConfiguration() {
        assertEquals(ConfNG.get(ApiConfig.API_BASE_URL), "https://api.example.com");
        assertEquals(ConfNG.getInt(ApiConfig.API_TIMEOUT), Integer.valueOf(5000));
        assertEquals(ConfNG.getInt(ApiConfig.API_RETRIES), Integer.valueOf(3));
    }

    @Test
    public void testSensitiveDataMasking() {
        String displayValue = ConfNG.getForDisplay(DatabaseConfig.DB_PASSWORD);
        assertEquals(displayValue, "***MASKED***");
    }
}
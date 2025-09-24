package org.confng.playground.testng;

/**
 * API Configuration Test demonstrating API-related configurations with TestNG data providers.
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.confng.playground.testng.ApiConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

@Test(groups = {"api-config"})
public class ApiConfigurationTest {

    @BeforeClass
    public void setupApiConfig() {
        System.setProperty("api.baseUrl", "https://test-api.example.com");
        System.setProperty("api.timeout", "10000");
        System.setProperty("api.retryCount", "5");
        System.setProperty("api.key", "test-api-key-12345");
        System.setProperty("api.version", "v2");
    }

    @DataProvider(name = "apiConfigData")
    public Object[][] apiConfigDataProvider() {
        return new Object[][]{
                {ApiConfig.API_BASE_URL, "https://test-api.example.com"},
                {ApiConfig.API_VERSION, "v2"}
        };
    }

    @Test(dataProvider = "apiConfigData")
    public void testApiStringConfigurations(ApiConfig config, String expected) {
        String actual = ConfNG.get(config);
        assertEquals(actual, expected);
    }

    @DataProvider(name = "apiIntConfigData")
    public Object[][] apiIntConfigDataProvider() {
        return new Object[][]{
                {ApiConfig.API_TIMEOUT, 10000},
                {ApiConfig.API_RETRY_COUNT, 5}
        };
    }

    @Test(dataProvider = "apiIntConfigData")
    public void testApiIntegerConfigurations(ApiConfig config, Integer expected) {
        Integer actual = ConfNG.getInt(config);
        assertEquals(actual, expected);
    }

    @Test
    public void testApiUrlValidation() {
        String baseUrl = ConfNG.get(ApiConfig.API_BASE_URL);

        assertThat(baseUrl)
                .startsWith("https://")
                .contains("api")
                .endsWith(".com");
    }

    @Test
    public void testApiTimeoutValidation() {
        Integer timeout = ConfNG.getInt(ApiConfig.API_TIMEOUT);

        assertThat(timeout)
                .isGreaterThan(0)
                .isLessThanOrEqualTo(60000);
    }

    @Test
    public void testApiRetryValidation() {
        Integer retryCount = ConfNG.getInt(ApiConfig.API_RETRY_COUNT);

        assertThat(retryCount)
                .isBetween(1, 10);
    }

    @Test
    public void testApiKeySecurity() {
        String apiKey = ConfNG.get(ApiConfig.API_KEY);
        String maskedKey = ConfNG.getForDisplay(ApiConfig.API_KEY);

        assertNotNull(apiKey);
        assertEquals(maskedKey, "***MASKED***");
        assertTrue(apiKey.length() >= 10);
    }

    @AfterClass
    public void cleanupApiConfig() {
        System.clearProperty("api.baseUrl");
        System.clearProperty("api.timeout");
        System.clearProperty("api.retryCount");
        System.clearProperty("api.key");
        System.clearProperty("api.version");
    }
}

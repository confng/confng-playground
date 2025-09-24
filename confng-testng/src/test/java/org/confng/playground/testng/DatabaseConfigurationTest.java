package org.confng.playground.testng;

/**
 * Database Configuration Test demonstrating database-related configurations with TestNG.
 *
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */

import org.confng.ConfNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test(groups = {"database-config"})
public class DatabaseConfigurationTest {

    @BeforeClass
    public void setupDatabaseConfig() {
        System.setProperty("database.url", "jdbc:postgresql://localhost:5432/testdb");
        System.setProperty("database.username", "testuser");
        System.setProperty("database.password", "secret123");
        System.setProperty("database.poolSize", "10");
        System.setProperty("database.timeout", "45000");
    }

    @Test(priority = 1)
    public void testStringConfiguration() {
        String appName = ConfNG.get(BasicConfig.APP_NAME);
        assertEquals(appName, "abcde");
    }

    @Test
    public void testDatabaseUrl() {
        String dbUrl = ConfNG.get(DatabaseConfig.DB_URL);
        assertEquals(dbUrl, "jdbc:postgresql://localhost:5432/testdb");
        assertTrue(dbUrl.startsWith("jdbc:"));
    }

    @Test
    public void testDatabaseCredentials() {
        String username = ConfNG.get(DatabaseConfig.DB_USERNAME);
        String password = ConfNG.get(DatabaseConfig.DB_PASSWORD);

        assertEquals(username, "testuser");
        assertEquals(password, "secret123");
    }

    @Test
    public void testDatabasePoolConfiguration() {
        Integer poolSize = ConfNG.getInt(DatabaseConfig.DB_POOL_SIZE);
        Integer timeout = ConfNG.getInt(DatabaseConfig.DB_TIMEOUT);

        assertEquals(poolSize, Integer.valueOf(10));
        assertEquals(timeout, Integer.valueOf(45000));

        assertTrue(poolSize > 0 && poolSize <= 100);
        assertTrue(timeout >= 1000);
    }

    @Test
    public void testSensitiveDataMasking() {
        String maskedPassword = ConfNG.getForDisplay(DatabaseConfig.DB_PASSWORD);
        String displayUsername = ConfNG.getForDisplay(DatabaseConfig.DB_USERNAME);

        assertEquals(maskedPassword, "***MASKED***");
        assertEquals(displayUsername, "testuser"); // Not sensitive
    }

    @Test
    public void testDatabaseConfigurationValidation() {
        SoftAssert softAssert = new SoftAssert();

        String url = ConfNG.get(DatabaseConfig.DB_URL);
        String username = ConfNG.get(DatabaseConfig.DB_USERNAME);
        Integer poolSize = ConfNG.getInt(DatabaseConfig.DB_POOL_SIZE);

        softAssert.assertNotNull(url, "Database URL should not be null");
        softAssert.assertNotNull(username, "Database username should not be null");
        softAssert.assertTrue(poolSize > 0, "Pool size should be positive");

        softAssert.assertAll();
    }

    @AfterClass
    public void cleanupDatabaseConfig() {
        System.clearProperty("database.url");
        System.clearProperty("database.username");
        System.clearProperty("database.password");
        System.clearProperty("database.poolSize");
        System.clearProperty("database.timeout");
    }
}

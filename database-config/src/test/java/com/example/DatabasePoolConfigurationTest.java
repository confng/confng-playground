package com.example;

/**
 * Test class for database pool configuration settings using ConfNG framework.
 * This class validates database connection pool parameters and optimization settings.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import com.example.config.DatabasePoolConfig;
import com.example.setup.DatabaseConfigurationSetup;
import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for database pool configuration settings
 */
public class DatabasePoolConfigurationTest {

    @BeforeClass
    public void setup() {
        DatabaseConfigurationSetup.setupDatabaseConfiguration();
    }

    @Test
    public void testDatabasePoolMinSize() {
        assertEquals(ConfNG.getInt(DatabasePoolConfig.DB_POOL_MIN_SIZE), Integer.valueOf(5));
    }

    @Test
    public void testDatabasePoolMaxSize() {
        assertEquals(ConfNG.getInt(DatabasePoolConfig.DB_POOL_MAX_SIZE), Integer.valueOf(20));
    }

    @Test
    public void testDatabaseTimeout() {
        assertEquals(ConfNG.getInt(DatabasePoolConfig.DB_TIMEOUT), Integer.valueOf(30000));
    }
}
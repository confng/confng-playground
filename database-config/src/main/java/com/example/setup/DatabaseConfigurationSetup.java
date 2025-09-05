package com.example.setup;

/**
 * Utility class for setting up database configuration for testing using ConfNG framework.
 * This class provides static methods to initialize database-related system properties for test scenarios.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public class DatabaseConfigurationSetup {
    
    public static void setupDatabaseConfiguration() {
        // Application settings
        System.setProperty("app.name", "ConfNG Database Example");
        System.setProperty("app.version", "3.0.0");
        System.setProperty("app.debug", "false");
        
        // Database pool settings
        System.setProperty("database.pool.min-size", "5");
        System.setProperty("database.pool.max-size", "20");
        System.setProperty("database.timeout", "30000");
        
        // API and security settings
        System.setProperty("api.rate-limit", "1000");
        System.setProperty("api.timeout", "15000");
        System.setProperty("api.secret-key", "sk-secret-database-key-12345");
        System.setProperty("encryption.key", "db-encryption-key-xyz789");
        
        // Feature flags
        System.setProperty("features.new-ui", "true");
        System.setProperty("features.beta-api", "false");
        System.setProperty("features.analytics", "true");
    }
}
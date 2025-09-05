# Database Configuration Example

This example demonstrates how to use ConfNG with a database as a configuration source, enabling dynamic configuration management and environment-specific settings.

## Features Demonstrated

- Custom database configuration source
- Environment-specific configuration
- Dynamic configuration loading
- Configuration caching for performance
- Sensitive data handling in database
- Feature flags management
- Configuration versioning and auditing

## Database Schema

The example uses a simple but powerful configuration table:

```sql
CREATE TABLE app_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(255) NOT NULL UNIQUE,
    config_value TEXT,
    environment VARCHAR(50) DEFAULT 'default',
    is_sensitive BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'system',
    description TEXT
);
```

## Key Features

### Environment-Specific Configuration
```sql
-- Default configuration
INSERT INTO app_config (config_key, config_value, environment) 
VALUES ('database.pool.max-size', '20', 'default');

-- Production override
INSERT INTO app_config (config_key, config_value, environment) 
VALUES ('database.pool.max-size', '50', 'production');
```

### Sensitive Data Handling
```sql
INSERT INTO app_config (config_key, config_value, is_sensitive) 
VALUES ('api.secret-key', 'sk-secret-key-123', TRUE);
```

### Custom Database Source
```java
public class DatabaseConfigSource implements ConfigSource {
    @Override
    public String getValue(String key) {
        return configCache.get(key);
    }
    
    @Override
    public int getPriority() {
        return 50; // Medium priority
    }
    
    public void refresh() {
        // Reload configuration from database
        loadConfiguration();
    }
}
```

### Configuration Registration
```java
@BeforeClass
public void setup() {
    DatabaseConfigSource dbSource = new DatabaseConfigSource(
        jdbcUrl, username, password, "production");
    ConfNG.addSource(dbSource);
}
```

## Use Cases

### 1. Feature Flags
```java
public enum FeatureFlags implements ConfNGKey {
    NEW_UI("features.new-ui", "false"),
    BETA_API("features.beta-api", "false"),
    ANALYTICS("features.analytics", "false");
}

// Usage
if (ConfNG.getBoolean(FeatureFlags.NEW_UI)) {
    // Show new UI
}
```

### 2. Environment-Specific Settings
```java
// Development
DatabaseConfigSource devSource = new DatabaseConfigSource(jdbcUrl, user, pass, "development");

// Production  
DatabaseConfigSource prodSource = new DatabaseConfigSource(jdbcUrl, user, pass, "production");
```

### 3. Dynamic Configuration Updates
```java
// Update configuration in database
UPDATE app_config 
SET config_value = 'true' 
WHERE config_key = 'features.new-ui' AND environment = 'production';

// Refresh configuration source
dbSource.refresh();

// New value is immediately available
boolean newUI = ConfNG.getBoolean(FeatureFlags.NEW_UI);
```

## Real-world Implementation

### PostgreSQL Example
```java
public class PostgreSQLConfigSource implements ConfigSource {
    private final DataSource dataSource;
    private final String environment;
    
    public PostgreSQLConfigSource(DataSource dataSource, String environment) {
        this.dataSource = dataSource;
        this.environment = environment;
    }
    
    @Override
    public String getValue(String key) {
        String sql = """
            SELECT config_value 
            FROM app_config 
            WHERE config_key = ? 
            AND (environment = ? OR environment = 'default')
            ORDER BY CASE WHEN environment = ? THEN 1 ELSE 2 END
            LIMIT 1
            """;
            
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, key);
            stmt.setString(2, environment);
            stmt.setString(3, environment);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("config_value") : null;
            }
        } catch (SQLException e) {
            logger.warn("Failed to retrieve config: " + key, e);
            return null;
        }
    }
}
```

### MySQL Example
```java
public class MySQLConfigSource implements ConfigSource {
    private final HikariDataSource dataSource;
    
    public MySQLConfigSource(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        this.dataSource = new HikariDataSource(config);
    }
    
    // Implementation similar to PostgreSQL
}
```

### Configuration Management API
```java
@RestController
@RequestMapping("/api/config")
public class ConfigController {
    
    @Autowired
    private DatabaseConfigSource configSource;
    
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshConfig() {
        configSource.refresh();
        return ResponseEntity.ok("Configuration refreshed");
    }
    
    @PutMapping("/{key}")
    public ResponseEntity<String> updateConfig(
            @PathVariable String key, 
            @RequestBody String value,
            @RequestParam String environment) {
        
        // Update database
        configRepository.updateConfig(key, value, environment);
        
        // Refresh configuration
        configSource.refresh();
        
        return ResponseEntity.ok("Configuration updated");
    }
}
```

## Performance Considerations

### 1. Configuration Caching
```java
private final Map<String, String> configCache = new ConcurrentHashMap<>();
private volatile long lastRefresh = 0;
private final long refreshInterval = 60000; // 1 minute

@Override
public String getValue(String key) {
    if (System.currentTimeMillis() - lastRefresh > refreshInterval) {
        refresh();
    }
    return configCache.get(key);
}
```

### 2. Connection Pooling
```java
// Use connection pooling for better performance
HikariConfig config = new HikariConfig();
config.setJdbcUrl(jdbcUrl);
config.setMaximumPoolSize(10);
config.setMinimumIdle(2);
config.setConnectionTimeout(30000);
DataSource dataSource = new HikariDataSource(config);
```

### 3. Batch Loading
```java
private void loadConfiguration() {
    String sql = "SELECT config_key, config_value FROM app_config WHERE environment IN (?, 'default')";
    
    // Load all configuration in one query
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, environment);
        // Process all results at once
    }
}
```

## Security Best Practices

### 1. Encrypt Sensitive Values
```sql
-- Store encrypted values in database
INSERT INTO app_config (config_key, config_value, is_sensitive) 
VALUES ('api.key', AES_ENCRYPT('actual-key', 'encryption-key'), TRUE);
```

### 2. Access Control
```sql
-- Create read-only user for application
CREATE USER 'config_reader'@'%' IDENTIFIED BY 'secure_password';
GRANT SELECT ON myapp.app_config TO 'config_reader'@'%';
```

### 3. Audit Trail
```sql
-- Add audit columns
ALTER TABLE app_config ADD COLUMN updated_by VARCHAR(100);
ALTER TABLE app_config ADD COLUMN change_reason TEXT;

-- Create audit trigger
CREATE TRIGGER config_audit 
BEFORE UPDATE ON app_config 
FOR EACH ROW 
SET NEW.updated_at = NOW(), NEW.updated_by = USER();
```

## Running the Example

```bash
cd examples/database-config
./gradlew test
```

## Expected Output

All tests should pass, demonstrating:
- ✅ Database configuration loading
- ✅ Environment-specific configuration
- ✅ Sensitive data masking
- ✅ Feature flags management
- ✅ Default value fallback
- ✅ Configuration caching

## Production Deployment

### Docker Compose
```yaml
version: '3.8'
services:
  app:
    image: myapp:latest
    environment:
      - DB_CONFIG_URL=jdbc:postgresql://config-db:5432/config
      - DB_CONFIG_USER=config_reader
      - DB_CONFIG_PASSWORD=secure_password
      - APP_ENVIRONMENT=production
    depends_on:
      - config-db
      
  config-db:
    image: postgres:15
    environment:
      - POSTGRES_DB=config
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - ./schema.sql:/docker-entrypoint-initdb.d/schema.sql
```

### Kubernetes ConfigMap
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  database.config.url: "jdbc:postgresql://config-db:5432/config"
  database.config.user: "config_reader"
  app.environment: "production"
```
# Properties Configuration Example

This example demonstrates how to use ConfNG with Java properties files as configuration source.

## Features Demonstrated

- Loading configuration from .properties files
- Type-safe configuration keys with enums
- Different data types (String, Integer, Boolean)
- Sensitive data handling and masking
- Multiple properties files with precedence
- Default values for missing properties

## Configuration Structure

The example uses multiple properties files to demonstrate precedence:

### `application.properties` (Main configuration)
```properties
# Application Configuration
app.name=ConfNG Properties Example
app.version=1.0.0
app.debug=true
app.profile=development

# Database Configuration
database.url=jdbc:h2:mem:testdb
database.username=sa
database.password=
database.pool.min-size=5
database.pool.max-size=20

# WebDriver Configuration
webdriver.browser=chrome
webdriver.headless=false
webdriver.timeout=30
webdriver.implicit-wait=10
```

### `database.properties` (Database-specific configuration)
```properties
# Database Connection Settings
database.driver=org.h2.Driver
database.connection-timeout=30000
database.validation-query=SELECT 1
database.pool.idle-timeout=600000
database.pool.max-lifetime=1800000

# Database Security
database.ssl.enabled=false
database.ssl.trust-store-path=
database.ssl.trust-store-password=
```

### `secrets.properties` (Sensitive configuration)
```properties
# API Keys and Secrets (marked as sensitive)
api.service1.secret-key=example_api_key_replace_with_real
api.service2.api-key=example_api_key_replace_with_real
oauth.client-secret=example_oauth_secret_replace_with_real

# Database Credentials
database.admin.username=admin
database.admin.password=example_password_replace_with_real
```

## Key Features

### Enum-based Configuration Keys
```java
public enum PropertiesConfig implements ConfNGKey {
    APP_NAME("app.name"),
    DATABASE_URL("database.url"),
    WEBDRIVER_BROWSER("webdriver.browser"),
    API_SERVICE1_SECRET_KEY("api.service1.secret-key", null, true); // Sensitive

    // Implementation details...
}
```

### Multiple Properties Files Loading
```java
@BeforeClass
public void setup() {
    // Load properties files in order (last loaded has higher precedence)
    ConfNG.loadProperties("src/test/resources/application.properties");
    ConfNG.loadProperties("src/test/resources/database.properties");
    ConfNG.loadProperties("src/test/resources/secrets.properties");
}
```

### Type-safe Access
```java
String appName = ConfNG.get(PropertiesConfig.APP_NAME);
Integer timeout = ConfNG.getInt(PropertiesConfig.WEBDRIVER_TIMEOUT);
Boolean debug = ConfNG.getBoolean(PropertiesConfig.APP_DEBUG);
```

### Sensitive Data Protection
```java
// Sensitive values are automatically masked
String maskedKey = ConfNG.getForDisplay(PropertiesConfig.API_SERVICE1_SECRET_KEY); // Returns "***MASKED***"
```

## Use Cases

### 1. Environment-Specific Properties
```properties
# application-dev.properties
app.profile=development
database.url=jdbc:h2:mem:devdb
app.debug=true

# application-prod.properties  
app.profile=production
database.url=jdbc:postgresql://prod-db:5432/myapp
app.debug=false
```

### 2. Feature Configuration
```properties
# Feature flags
features.new-ui=true
features.beta-api=false
features.analytics=true
features.caching=true

# Performance settings
cache.ttl=3600
cache.max-size=1000
thread.pool.size=10
```

### 3. Integration Settings
```properties
# External service configuration
service.payment.url=https://api.example-payment.com
service.payment.timeout=30000
service.payment.retry-attempts=3

service.email.url=https://api.example-email.com
service.email.timeout=15000
service.email.batch-size=100
```

## Properties File Precedence

When multiple properties files are loaded, the last loaded file takes precedence:

1. `application.properties` (Base configuration)
2. `database.properties` (Database-specific overrides)
3. `secrets.properties` (Sensitive data overrides)

If the same key exists in multiple files, the value from the last loaded file wins.

## Running the Example

```bash
cd playground/properties-config
./gradlew test
```

## Expected Output

All tests should pass, demonstrating:
- ✅ Properties file loading and parsing
- ✅ Type conversion (String, Integer, Boolean)
- ✅ Multiple properties files with precedence
- ✅ Sensitive data masking
- ✅ Default value fallback

## Real-world Usage

### Spring Boot Style Configuration
```properties
# Server configuration
server.port=8080
server.servlet.context-path=/api

# Logging configuration
logging.level.org.confng=DEBUG
logging.file.name=application.log

# Management endpoints
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

### Maven/Gradle Integration
```gradle
test {
    systemProperty 'config.file', 'src/test/resources/test.properties'
    systemProperty 'app.profile', 'test'
}
```

### Docker Configuration
```dockerfile
# Copy properties files
COPY application.properties /app/config/
COPY application-prod.properties /app/config/

# Set configuration path
ENV CONFIG_PATH=/app/config/application.properties
```

## Best Practices

1. **Organize by Domain**: Separate properties files by functional area
2. **Use Hierarchical Keys**: Use dot notation for nested configuration
3. **Mark Sensitive Data**: Always mark passwords and API keys as sensitive
4. **Provide Defaults**: Include sensible default values in enum definitions
5. **Document Properties**: Add comments explaining configuration options
6. **Environment Separation**: Use different properties files per environment

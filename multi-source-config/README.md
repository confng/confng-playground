# Multi-Source Configuration Example

This example demonstrates how ConfNG handles multiple configuration sources with proper precedence ordering and source chaining.

## Features Demonstrated

- Multiple configuration sources working together
- Source precedence and priority ordering
- Configuration override patterns
- Environment-specific configuration layering
- Source registration and management
- Dynamic source addition and removal

## Configuration Sources Used

This example combines multiple configuration sources to show realistic configuration management:

1. **Environment Variables** (Priority: 60) - Highest precedence for deployment
2. **System Properties** (Priority: 50) - Command-line overrides
3. **JSON Configuration** (Priority: 30) - Structured application config
4. **Properties Files** (Priority: 25) - Traditional Java configuration
5. **Default Values** (Priority: 0) - Fallback configuration

## Source Precedence Flow

```
Environment Variables (60)
        ↓ (if not found)
System Properties (50)
        ↓ (if not found)
JSON Configuration (30)
        ↓ (if not found)
Properties Files (25)
        ↓ (if not found)
Default Values (0)
```

## Configuration Files

### `config.json` (Application Configuration)
```json
{
  "app": {
    "name": "Multi-Source ConfNG Example",
    "version": "2.0.0",
    "environment": "development"
  },
  "database": {
    "url": "jdbc:postgresql://localhost:5432/devdb",
    "pool": {
      "maxSize": 15,
      "minSize": 3
    }
  },
  "features": {
    "newUI": true,
    "analytics": false
  }
}
```

### `application.properties` (Base Configuration)
```properties
# Base application settings
app.name=Properties ConfNG Example
app.timeout=30000
app.retry.attempts=3

# Database fallback configuration
database.url=jdbc:h2:mem:fallbackdb
database.username=fallback_user
database.pool.maxSize=10

# Feature defaults
features.newUI=false
features.caching=true
```

## Key Features

### Source Registration and Priority
```java
@BeforeClass
public void setupMultipleSources() {
    ConfNG.clearSourcesAndUseDefaults(); // Starts with Env + System Properties
    
    // Add JSON source (will be ordered by priority)
    ConfNG.loadJson("src/test/resources/config.json");
    
    // Add Properties source (lower priority than JSON)
    ConfNG.loadProperties("src/test/resources/application.properties");
}
```

### Configuration Override Patterns
```java
// This value can come from multiple sources:
// 1. Environment variable: APP_NAME
// 2. System property: app.name
// 3. JSON: app.name
// 4. Properties: app.name
// 5. Default value
String appName = ConfNG.get(MultiSourceConfig.APP_NAME);
```

### Environment-Specific Layering
```bash
# Development
export APP_ENVIRONMENT=development
export DATABASE_URL=jdbc:postgresql://dev-db:5432/myapp

# Production  
export APP_ENVIRONMENT=production
export DATABASE_URL=jdbc:postgresql://prod-db:5432/myapp
export FEATURES_ANALYTICS=true
```

## Use Cases

### 1. Development vs Production Configuration
```java
// Base configuration in properties/JSON
// Override with environment variables for deployment
public void testEnvironmentOverrides() {
    // JSON says: "development", but env var can override to "production"
    String environment = ConfNG.get(MultiSourceConfig.APP_ENVIRONMENT);
}
```

### 2. Feature Flag Management
```java
// Feature flags can be controlled at multiple levels:
// - JSON for default application behavior
// - System properties for testing (-Dfeatures.newUI=true)
// - Environment variables for deployment (FEATURES_NEW_UI=false)
public void testFeatureFlags() {
    Boolean newUI = ConfNG.getBoolean(MultiSourceConfig.FEATURES_NEW_UI);
    Boolean analytics = ConfNG.getBoolean(MultiSourceConfig.FEATURES_ANALYTICS);
}
```

### 3. Database Configuration Layering
```java
// Database config priority:
// 1. Environment: DATABASE_URL (for containers/cloud)
// 2. System properties: database.url (for local testing)
// 3. JSON: database.url (for application defaults)
// 4. Properties: database.url (for fallback)
public void testDatabaseConfig() {
    String dbUrl = ConfNG.get(MultiSourceConfig.DATABASE_URL);
    Integer maxPool = ConfNG.getInt(MultiSourceConfig.DATABASE_POOL_MAX_SIZE);
}
```

## Source Management

### Dynamic Source Addition
```java
@Test
public void testDynamicSourceAddition() {
    // Add a custom high-priority source
    CustomConfigSource customSource = new CustomConfigSource();
    ConfNG.addSource(customSource); // Automatically ordered by priority
    
    // Verify the new source takes precedence
    String value = ConfNG.get(MultiSourceConfig.CUSTOM_VALUE);
    assertThat(value).isEqualTo("from-custom-source");
}
```

### Source Priority Verification
```java
@Test
public void testSourcePrecedence() {
    // Set the same key in multiple sources and verify precedence
    System.setProperty("test.precedence", "system-property");
    // Environment variable TEST_PRECEDENCE would override this
    // JSON/Properties would be lower priority
    
    String value = ConfNG.get(MultiSourceConfig.TEST_PRECEDENCE);
    // Should get environment variable if set, otherwise system property
}
```

## Configuration Scenarios

### Scenario 1: Local Development
```bash
# No environment variables set
# System properties: -Dapp.environment=local
# JSON provides: app.name, database.url
# Properties provides: fallback values
```

### Scenario 2: CI/CD Testing
```bash
# Environment: APP_ENVIRONMENT=test, DATABASE_URL=jdbc:h2:mem:test
# System properties: -Dfeatures.analytics=false
# JSON/Properties: provide base configuration
```

### Scenario 3: Production Deployment
```bash
# Environment: Full production config via env vars
# System properties: None (or minimal)
# JSON/Properties: Application defaults only
```

## Running the Example

```bash
cd playground/multi-source-config
./gradlew test
```

### Test with Different Configurations

```bash
# Test with system property overrides
./gradlew test -Dapp.environment=test -Dfeatures.newUI=false

# Test with environment variables
export APP_NAME="Override App Name"
export DATABASE_POOL_MAX_SIZE=50
./gradlew test
```

## Expected Output

All tests should pass, demonstrating:
- ✅ Multiple source integration
- ✅ Proper precedence ordering
- ✅ Environment variable overrides
- ✅ System property overrides
- ✅ JSON and Properties fallbacks
- ✅ Default value handling
- ✅ Dynamic source management

## Best Practices Demonstrated

1. **Layered Configuration**: Use multiple sources for different concerns
2. **Environment Separation**: Higher priority sources for deployment-specific config
3. **Graceful Fallbacks**: Lower priority sources provide sensible defaults
4. **Source Ordering**: Understand and leverage priority-based resolution
5. **Dynamic Management**: Add/remove sources as needed for different scenarios
6. **Testing Strategies**: Test configuration behavior across different source combinations

## Real-world Applications

### Microservice Configuration
```yaml
# Docker Compose
environment:
  - APP_NAME=user-service
  - DATABASE_URL=jdbc:postgresql://db:5432/users
  - FEATURES_ANALYTICS=true
```

### Kubernetes Deployment
```yaml
# ConfigMap for base config
# Secrets for sensitive data
# Environment variables for instance-specific config
```

### Spring Boot Integration
```java
// ConfNG can complement Spring's configuration
// Use for non-Spring components or custom configuration needs
```
# YAML Configuration Example

This example demonstrates how to extend ConfNG with a custom YAML configuration source, showing how to implement custom configuration sources for formats not natively supported.

## Features Demonstrated

- Custom configuration source implementation
- YAML file parsing and configuration loading
- Nested YAML structure access with dot notation
- Integration with existing ConfNG sources
- Type-safe configuration with custom sources
- Error handling for malformed YAML files

## YAML Configuration Structure

The example uses a comprehensive YAML configuration file:

### `application.yaml`
```yaml
app:
  name: "ConfNG YAML Example"
  version: "1.0.0"
  environment: "development"
  debug: true
  features:
    - "new-ui"
    - "analytics"
    - "caching"

database:
  primary:
    url: "jdbc:postgresql://localhost:5432/primary"
    username: "primary_user"
    password: "primary_pass"
    pool:
      maxSize: 20
      minSize: 5
      timeout: 30000
  secondary:
    url: "jdbc:postgresql://localhost:5432/secondary"
    username: "secondary_user"
    password: "secondary_pass"

webdriver:
  browser: "chrome"
  headless: false
  timeout: 45
  capabilities:
    - name: "chrome.switches"
      value: ["--disable-web-security", "--no-sandbox"]
    - name: "chrome.prefs"
      value: 
        download.default_directory: "/tmp/downloads"

api:
  services:
    payment:
      baseUrl: "https://api.stripe.com"
      timeout: 15000
      retryAttempts: 3
    notification:
      baseUrl: "https://api.sendgrid.com"
      timeout: 10000
      retryAttempts: 2

monitoring:
  metrics:
    enabled: true
    interval: 60000
    exporters:
      - "prometheus"
      - "datadog"
  logging:
    level: "INFO"
    appenders:
      - type: "console"
        pattern: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
      - type: "file"
        file: "application.log"
        maxSize: "10MB"
```

## Key Features

### Custom YAML Source Implementation
```java
public class YamlSource implements ConfigSource {
    private final Map<String, Object> yamlData;
    
    public YamlSource(Path yamlFile) {
        this.yamlData = loadYamlFile(yamlFile);
    }
    
    @Override
    public Optional<String> get(String key) {
        Object value = getNestedValue(yamlData, key);
        return value != null ? Optional.of(String.valueOf(value)) : Optional.empty();
    }
    
    @Override
    public int getPriority() {
        return 35; // Between JSON (30) and System Properties (50)
    }
}
```

### Nested Property Access
```java
// Access nested YAML properties using dot notation
String dbUrl = ConfNG.get(YamlConfig.DATABASE_PRIMARY_URL);        // database.primary.url
Integer poolMax = ConfNG.getInt(YamlConfig.DATABASE_POOL_MAX_SIZE); // database.primary.pool.maxSize
String apiUrl = ConfNG.get(YamlConfig.API_PAYMENT_BASE_URL);        // api.services.payment.baseUrl
```

### Complex Data Structure Handling
```java
// Handle YAML arrays and complex structures
List<String> features = yamlSource.getList("app.features");
Map<String, Object> capabilities = yamlSource.getMap("webdriver.capabilities");
```

## YAML Source Integration

### Source Registration
```java
@BeforeClass
public void setup() {
    ConfNG.clearSourcesAndUseDefaults();
    
    // Add YAML source with custom priority
    YamlSource yamlSource = new YamlSource(Paths.get("src/test/resources/application.yaml"));
    ConfNG.addSource(yamlSource);
    
    // YAML will be ordered by priority between other sources
}
```

### Error Handling
```java
public class YamlSource implements ConfigSource {
    public YamlSource(Path yamlFile) {
        try {
            this.yamlData = loadYamlFile(yamlFile);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load YAML file: " + yamlFile, e);
        }
    }
    
    private Map<String, Object> loadYamlFile(Path file) throws IOException {
        if (!Files.exists(file)) {
            return Collections.emptyMap(); // Graceful handling of missing files
        }
        
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Files.newInputStream(file)) {
            return yaml.load(inputStream);
        }
    }
}
```

## Use Cases

### 1. Kubernetes Configuration
```yaml
# kubernetes-config.yaml
kubernetes:
  namespace: "production"
  deployment:
    replicas: 3
    resources:
      requests:
        memory: "512Mi"
        cpu: "250m"
      limits:
        memory: "1Gi"
        cpu: "500m"
  service:
    type: "LoadBalancer"
    port: 8080
```

### 2. CI/CD Pipeline Configuration
```yaml
# pipeline-config.yaml
pipeline:
  stages:
    - name: "build"
      timeout: 600
      parallel: true
    - name: "test"
      timeout: 1200
      coverage:
        threshold: 80
    - name: "deploy"
      timeout: 300
      environments:
        - "staging"
        - "production"
```

### 3. Microservice Configuration
```yaml
# microservice-config.yaml
service:
  name: "user-service"
  port: 8080
  health:
    endpoint: "/health"
    interval: 30
  
dependencies:
  - name: "auth-service"
    url: "http://auth-service:8081"
    timeout: 5000
  - name: "notification-service"
    url: "http://notification-service:8082"
    timeout: 3000
```

## Advanced YAML Features

### 1. YAML Anchors and References
```yaml
# Define reusable configurations
database_defaults: &database_defaults
  pool:
    maxSize: 20
    minSize: 5
    timeout: 30000
  ssl:
    enabled: true

database:
  primary:
    <<: *database_defaults
    url: "jdbc:postgresql://primary:5432/db"
  secondary:
    <<: *database_defaults
    url: "jdbc:postgresql://secondary:5432/db"
```

### 2. Environment-specific YAML
```yaml
# application-${environment}.yaml
app:
  environment: "${ENVIRONMENT:development}"
  
database:
  url: "${DATABASE_URL:jdbc:h2:mem:testdb}"
  username: "${DATABASE_USER:sa}"
  password: "${DATABASE_PASSWORD:}"
```

### 3. Multi-document YAML
```yaml
# Multiple configurations in one file
---
# Development configuration
app:
  name: "Dev App"
  debug: true

---
# Production configuration  
app:
  name: "Prod App"
  debug: false
```

## Implementation Details

### YAML Parsing with SnakeYAML
```java
dependencies {
    implementation 'org.yaml:snakeyaml:2.0'
}
```

### Nested Value Resolution
```java
private Object getNestedValue(Map<String, Object> data, String key) {
    String[] parts = key.split("\\.");
    Object current = data;
    
    for (String part : parts) {
        if (current instanceof Map) {
            current = ((Map<?, ?>) current).get(part);
        } else {
            return null;
        }
    }
    
    return current;
}
```

### Type Conversion Support
```java
public <T> Optional<T> getTyped(String key, Class<T> type) {
    Object value = getNestedValue(yamlData, key);
    if (value == null) return Optional.empty();
    
    if (type.isInstance(value)) {
        return Optional.of(type.cast(value));
    }
    
    // Handle string conversion
    if (type == String.class) {
        return Optional.of(type.cast(String.valueOf(value)));
    }
    
    // Add more type conversions as needed
    return Optional.empty();
}
```

## Running the Example

```bash
cd playground/yaml-config
./gradlew test
```

## Expected Output

All tests should pass, demonstrating:
- ✅ YAML file loading and parsing
- ✅ Nested property access with dot notation
- ✅ Integration with ConfNG source precedence
- ✅ Type conversion from YAML values
- ✅ Error handling for malformed YAML
- ✅ Complex data structure handling

## Best Practices

1. **Graceful Degradation**: Handle missing YAML files gracefully
2. **Type Safety**: Provide proper type conversion methods
3. **Error Messages**: Give clear error messages for YAML parsing issues
4. **Performance**: Cache parsed YAML data for repeated access
5. **Validation**: Validate YAML structure against expected schema
6. **Documentation**: Document YAML structure and expected format

## Integration with Other Tools

### Spring Boot
```java
// Use alongside Spring's YAML support
@Configuration
public class ConfNGConfig {
    @PostConstruct
    public void setupConfNG() {
        ConfNG.addSource(new YamlSource(Paths.get("application.yaml")));
    }
}
```

### Docker Compose
```yaml
# Mount YAML config as volume
volumes:
  - ./config/application.yaml:/app/config/application.yaml
```

### Kubernetes ConfigMap
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  application.yaml: |
    app:
      name: "Kubernetes App"
      environment: "production"
```
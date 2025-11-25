# Environment Auto-Loading Example

This example demonstrates the **new automatic environment loading features** in ConfNG, including:

- ✨ **Automatic environment detection** via `autoLoadConfig()`
- ✨ **Global/common configuration loading** via `loadGlobalConfig()`
- ✨ **Case-insensitive environment key detection** (APP_ENV, app_env, App_Env, etc.)
- ✨ **Multi-source environment configuration** (TestNG, env vars, system properties, config files)
- ✨ **Configuration precedence and override patterns**

## 🎯 Features Demonstrated

### 1. Automatic Environment Detection

ConfNG now automatically detects the current environment by checking multiple configuration sources:

```java
// Automatically detects environment from:
// 1. TestNG parameters (highest priority)
// 2. Environment variables
// 3. System properties
// 4. Configuration files
// 5. Defaults to "local"
String environment = ConfNG.autoLoadConfig();
```

### 2. Global Configuration Loading

Load common configuration that applies to ALL environments:

```java
// Loads global.properties, global.json, global.yaml, global.toml
// Also loads common.properties, common.json, common.yaml, common.toml
ConfNG.loadGlobalConfig();
```

### 3. Case-Insensitive Environment Keys

Environment can be set using ANY case variation:

```bash
export APP_ENV=uat      # ✅ Works
export app_env=uat      # ✅ Works
export App_Env=uat      # ✅ Works
export aPp_EnV=uat      # ✅ Works
```

```xml
<!-- TestNG XML: Use 'environment' or 'env' parameter for automatic loading -->
<parameter name="environment" value="uat"/>  <!-- ✅ Recommended -->
<parameter name="env" value="uat"/>          <!-- ✅ Also works -->
```

**Note:** For TestNG XML automatic loading, use `environment` or `env` parameter names.
The case-insensitive detection (APP_ENV, app_env, etc.) applies to environment variables
and system properties.

### 4. Configuration Precedence Order

Higher priority sources override lower priority sources:

| Priority | Source | Example |
|----------|--------|---------|
| **Highest** | TestNG Parameters (80+) | `<parameter name="environment" value="uat"/>` |
| ↓ | Environment Variables (60) | `export APP_ENV=production` |
| ↓ | System Properties (50) | `-DAPP_ENV=staging` |
| ↓ | Configuration Files (30) | `app.environment=qa` |
| **Lowest** | Default Values (0) | `"local"` |

## 📁 Configuration Files

### `global.properties` (Base Configuration)

Common settings that apply to ALL environments:

```properties
# Application defaults
app.name=ConfNG Environment Auto-Loading Example
app.version=1.0.0
app.timeout=30000
app.retry.count=3

# Cache settings
cache.enabled=true
cache.ttl=300
cache.maxSize=1000

# Feature flags (global defaults)
features.newUI=false
features.analytics=false
features.caching=true
```

### `uat.properties` (UAT Environment)

UAT-specific settings that **override** global.properties:

```properties
# UAT-specific overrides
app.environment=uat
api.url=https://uat-api.example.com
api.timeout=60000

# UAT database
database.url=jdbc:postgresql://uat-db.example.com:5432/myapp_uat
database.pool.maxSize=30

# UAT feature flags
features.newUI=true
features.analytics=true
```

### `prod.properties` (Production Environment)

Production-specific settings:

```properties
# Production overrides
app.environment=production
api.url=https://api.example.com
database.url=jdbc:postgresql://prod-db.example.com:5432/myapp_prod
database.pool.maxSize=50
log.level=WARN
```

### `local.json` (Local Development)

Local development settings:

```json
{
  "app.environment": "local",
  "api.url": "http://localhost:8080",
  "database.url": "jdbc:h2:mem:testdb",
  "log.level": "DEBUG"
}
```

## 🚀 Running the Examples

### Run All Tests

```bash
cd playground/environment-auto-loading
./gradlew test
```

### Run UAT Environment Tests

```bash
./gradlew test -Dtestng.suite=src/test/resources/testng-uat.xml
```

### Run Production Environment Tests

```bash
./gradlew test -Dtestng.suite=src/test/resources/testng-prod.xml
```

### Override Environment via System Property

```bash
# Override the environment (case-insensitive!)
./gradlew test -DAPP_ENV=prod
./gradlew test -Dapp_env=uat
./gradlew test -DApp_Env=local
```

### Override Environment via Environment Variable

```bash
# Set environment variable (case-insensitive!)
export APP_ENV=uat
./gradlew test

export app_env=prod
./gradlew test
```

## 📚 Test Classes

### 1. `EnvironmentAutoLoadingTest`

Demonstrates automatic environment detection and configuration loading:

- ✅ Global configuration loading
- ✅ Environment-specific configuration overrides
- ✅ Configuration inheritance patterns
- ✅ Feature flag management
- ✅ Database configuration per environment

### 2. `CaseInsensitiveKeysTest`

Demonstrates case-insensitive environment key detection:

- ✅ APP_ENV, app_env, App_Env all work
- ✅ ENVIRONMENT, environment, Environment all work
- ✅ ENV, env, Env all work
- ✅ Cross-platform compatibility

### 3. `PrecedenceOrderTest`

Demonstrates configuration source precedence:

- ✅ TestNG parameters override everything
- ✅ Environment variables override system properties
- ✅ System properties override config files
- ✅ Config files override default values

### 4. `GlobalConfigTest`

Demonstrates global vs environment-specific configuration:

- ✅ Global configuration loading
- ✅ Environment-specific overrides
- ✅ Configuration inheritance
- ✅ Real-world use cases

## 🎓 Key Concepts

### Global → Environment-Specific Pattern

```
1. Load global.properties (base configuration)
   ↓
2. Load {env}.properties (environment-specific overrides)
   ↓
3. Result: Environment values override global values
           Global values inherited when not overridden
```

**Example:**

```properties
# global.properties
cache.ttl=300
api.timeout=10000
database.pool.maxSize=20

# uat.properties
api.timeout=60000
database.pool.maxSize=30
```

**Result in UAT:**
- `cache.ttl=300` ← inherited from global
- `api.timeout=60000` ← overridden by uat
- `database.pool.maxSize=30` ← overridden by uat

### Environment Detection Flow

```
Check APP_ENV (case-insensitive)
  ↓ (not found)
Check ENVIRONMENT (case-insensitive)
  ↓ (not found)
Check ENV (case-insensitive)
  ↓ (not found)
Default to "local"
```

For each key, check sources in precedence order:
1. TestNG Parameters
2. Environment Variables
3. System Properties
4. Configuration Files

### Case-Insensitive Matching

For each key (e.g., "APP_ENV"), ConfNG tries:
1. Original case: `APP_ENV`
2. Lowercase: `app_env`
3. Uppercase: `APP_ENV`
4. Title case: `App_Env`

## 💡 Real-World Use Cases

### Use Case 1: Multi-Environment Testing

```xml
<!-- testng-uat.xml -->
<suite name="UAT Tests">
    <parameter name="environment" value="uat"/>
    <!-- Tests run against UAT environment -->
</suite>

<!-- testng-prod.xml -->
<suite name="Production Tests">
    <parameter name="environment" value="prod"/>
    <!-- Tests run against production environment -->
</suite>
```

### Use Case 2: CI/CD Pipeline

```bash
# Development
export APP_ENV=dev
./gradlew test

# UAT
export APP_ENV=uat
./gradlew test

# Production
export APP_ENV=production
./gradlew test
```

### Use Case 3: Docker Deployment

```dockerfile
# Dockerfile
ENV APP_ENV=production
ENV API_URL=https://api.example.com
ENV DATABASE_URL=jdbc:postgresql://db:5432/myapp
```

### Use Case 4: Kubernetes ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  APP_ENV: "production"
  API_URL: "https://api.example.com"
```

## ✅ Expected Output

All tests should pass, demonstrating:

- ✅ Automatic environment detection
- ✅ Global configuration loading
- ✅ Environment-specific overrides
- ✅ Case-insensitive key matching
- ✅ Configuration precedence
- ✅ Multi-source configuration

## 🎉 Benefits

### 1. Simplified Configuration Management

```java
// Before: Manual environment detection
String env = System.getenv("APP_ENV");
if (env == null) env = System.getenv("ENVIRONMENT");
if (env == null) env = "local";
ConfNG.loadProperties(env + ".properties");

// After: Automatic!
ConfNG.loadGlobalConfig();
ConfNG.autoLoadConfig();
```

### 2. Flexible Environment Setting

Environment can be set via:
- ✅ TestNG parameters (best for testing)
- ✅ Environment variables (best for deployment)
- ✅ System properties (best for local dev)
- ✅ Configuration files (best for defaults)

### 3. Case-Insensitive = User-Friendly

No need to remember exact casing:
- ✅ `APP_ENV`, `app_env`, `App_Env` all work
- ✅ Works across different platforms
- ✅ Compatible with different conventions

### 4. Global + Environment Pattern

- ✅ Define common settings once (global.properties)
- ✅ Override per environment (uat.properties, prod.properties)
- ✅ Inherit values that don't change
- ✅ Maintainable and DRY

## 📖 Further Reading

- [ConfNG Documentation](../../README.md)
- [TestNG Integration](../confng-testng/README.md)
- [Multi-Source Configuration](../multi-source-config/README.md)


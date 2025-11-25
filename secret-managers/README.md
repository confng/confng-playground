# Secret Managers Example

This example demonstrates how to integrate ConfNG with secret management services like cloud provider secret managers or third-party vault solutions.

## Features Demonstrated

- Custom secret manager source implementation
- Sensitive data handling and masking
- High-priority configuration source
- Different types of secrets (API keys, passwords, certificates)
- Mock secret manager for testing

## Secret Types Covered

### Database Credentials
- Database passwords
- Connection strings
- User credentials

### API Keys
- Third-party service API keys
- Internal service API keys
- Authentication tokens

### OAuth & JWT
- OAuth client secrets
- JWT signing keys
- Refresh tokens

### Cloud Provider Credentials
- Cloud access keys and secret keys
- Service principal credentials
- Service account keys

## Key Features

### Custom Secret Manager Source
```java
public class MockSecretManagerSource extends SecretManagerSource {
    @Override
    public String getValue(String key) {
        // Connect to your secret manager service
        return secretManagerClient.getSecret(key);
    }
    
    @Override
    public int getPriority() {
        return 100; // High priority for secrets
    }
}
```

### Sensitive Data Protection
```java
public enum SecretConfig implements ConfNGKey {
    DB_PASSWORD("database/password", null, true),  // Marked as sensitive
    SERVICE1_API_KEY("api-keys/service1", null, true);
}

// Sensitive values are automatically masked
String masked = ConfNG.getForDisplay(SecretConfig.DB_PASSWORD); // Returns "***MASKED***"
```

### Configuration Source Registration
```java
@BeforeClass
public void setup() {
    SecretManagerSource secretManager = new CloudSecretsManagerSource("region-1");
    ConfNG.addSource(secretManager);
}
```

## Real-world Integration

### Cloud Secrets Manager
```java
public class CloudSecretsManagerSource extends SecretManagerSource {
    private final SecretsManagerClient client;

    public CloudSecretsManagerSource(String region) {
        super(region);
        this.client = SecretsManagerClient.builder()
            .region(Region.of(region))
            .build();
    }
    
    @Override
    public String getValue(String key) {
        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(key)
                .build();
            GetSecretValueResponse response = client.getSecretValue(request);
            return response.secretString();
        } catch (Exception e) {
            return null; // Secret not found
        }
    }
}
```

### Azure Key Vault
```java
public class AzureKeyVaultSource extends SecretManagerSource {
    private final SecretClient client;
    
    public AzureKeyVaultSource(String vaultUrl) {
        super("azure");
        this.client = new SecretClientBuilder()
            .vaultUrl(vaultUrl)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();
    }
    
    @Override
    public String getValue(String key) {
        try {
            KeyVaultSecret secret = client.getSecret(key);
            return secret.getValue();
        } catch (Exception e) {
            return null;
        }
    }
}
```

### HashiCorp Vault
```java
public class HashiCorpVaultSource extends SecretManagerSource {
    private final Vault vault;
    
    public HashiCorpVaultSource(String address, String token) {
        super("vault");
        VaultConfig config = new VaultConfig()
            .address(address)
            .token(token);
        this.vault = new Vault(config);
    }
    
    @Override
    public String getValue(String key) {
        try {
            LogicalResponse response = vault.logical().read("secret/" + key);
            return response.getData().get("value");
        } catch (Exception e) {
            return null;
        }
    }
}
```

## Security Best Practices

### 1. Mark Sensitive Keys
```java
public enum SecretConfig implements ConfNGKey {
    API_KEY("api/key", null, true),        // Always mark as sensitive
    DB_PASSWORD("db/password", null, true); // Prevents accidental logging
}
```

### 2. Use High Priority
```java
@Override
public int getPriority() {
    return 100; // Secrets should override other sources
}
```

### 3. Handle Errors Gracefully
```java
@Override
public String getValue(String key) {
    try {
        return secretManager.getSecret(key);
    } catch (SecretNotFoundException e) {
        return null; // Let ConfNG use default value
    } catch (Exception e) {
        logger.warn("Failed to retrieve secret: " + key, e);
        return null;
    }
}
```

### 4. Use Proper Authentication
- Cloud Provider: Use IAM roles or managed identities, not hardcoded credentials
- Vault Solutions: Use AppRole or Kubernetes auth
- Always use environment-specific authentication mechanisms

## Running the Example

```bash
cd examples/secret-managers
./gradlew test
```

## Expected Output

All tests should pass, demonstrating:
- ✅ Secret manager integration
- ✅ Sensitive data masking
- ✅ High-priority configuration source
- ✅ Different secret types handling
- ✅ Graceful error handling for missing secrets

## Production Deployment

### Environment Variables for Authentication
```bash
# Cloud Provider Example
export CLOUD_REGION=region-1
export CLOUD_ROLE_ARN=arn:cloud:iam::123456789:role/MyAppRole

# Alternative Cloud Provider
export CLOUD_CLIENT_ID=your-client-id
export CLOUD_TENANT_ID=your-tenant-id
export CLOUD_VAULT_URL=https://vault.example.com/

# Vault Solution
export VAULT_ADDR=https://vault.example.com:8200
export VAULT_TOKEN=your-vault-token
```

### Docker Integration
```dockerfile
# Install cloud provider CLI for authentication
RUN apt-get update && apt-get install -y cloud-cli

# Set environment variables
ENV CLOUD_REGION=region-1
ENV CLOUD_VAULT_URL=https://vault.example.com/

# Your application
COPY app.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
```

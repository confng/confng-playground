# Secret Managers Example

This example demonstrates how to integrate ConfNG with secret management services like AWS Secrets Manager, Azure Key Vault, or HashiCorp Vault.

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
- Third-party service API keys (Stripe, SendGrid, etc.)
- Internal service API keys
- Authentication tokens

### OAuth & JWT
- OAuth client secrets
- JWT signing keys
- Refresh tokens

### Cloud Provider Credentials
- AWS access keys and secret keys
- Azure service principal credentials
- GCP service account keys

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
    API_KEY("api-keys/stripe", null, true);
}

// Sensitive values are automatically masked
String masked = ConfNG.getForDisplay(SecretConfig.DB_PASSWORD); // Returns "***"
```

### Configuration Source Registration
```java
@BeforeClass
public void setup() {
    SecretManagerSource secretManager = new AWSSecretsManagerSource("us-west-2");
    ConfNG.addSource(secretManager);
}
```

## Real-world Integration

### AWS Secrets Manager
```java
public class AWSSecretsManagerSource extends SecretManagerSource {
    private final SecretsManagerClient client;
    
    public AWSSecretsManagerSource(String region) {
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
- AWS: Use IAM roles, not hardcoded credentials
- Azure: Use Managed Identity or Service Principal
- Vault: Use AppRole or Kubernetes auth

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
# AWS
export AWS_REGION=us-west-2
export AWS_ROLE_ARN=arn:aws:iam::123456789:role/MyAppRole

# Azure
export AZURE_CLIENT_ID=your-client-id
export AZURE_TENANT_ID=your-tenant-id
export AZURE_KEY_VAULT_URL=https://myvault.vault.azure.net/

# Vault
export VAULT_ADDR=https://vault.company.com
export VAULT_TOKEN=your-vault-token
```

### Docker Integration
```dockerfile
# Install AWS CLI or Azure CLI for authentication
RUN apt-get update && apt-get install -y awscli

# Set environment variables
ENV AWS_REGION=us-west-2
ENV AZURE_KEY_VAULT_URL=https://myvault.vault.azure.net/

# Your application
COPY app.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
```
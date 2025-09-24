package org.confng.playground.yamlconfig;

import org.confng.sources.ConfigSource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Custom configuration source that reads from YAML files.
 * 
 * <p>This source demonstrates how to extend ConfNG with custom configuration formats.
 * It uses SnakeYAML for parsing and supports nested property access using dot notation.</p>
 * 
 * <p>Features:</p>
 * <ul>
 *   <li>YAML file parsing with SnakeYAML</li>
 *   <li>Nested property access (e.g., "database.primary.url")</li>
 *   <li>Graceful handling of missing files</li>
 *   <li>Type conversion support</li>
 *   <li>Integration with ConfNG source precedence</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @since 1.0
 */
public class YamlSource implements ConfigSource {
    
    private final Map<String, Object> yamlData;
    private final String sourceName;
    
    /**
     * Creates a new YamlSource from the given YAML file.
     * 
     * @param yamlFile path to the YAML file
     * @throws IllegalStateException if the file cannot be loaded or parsed
     */
    public YamlSource(Path yamlFile) {
        this.sourceName = "YAML(" + yamlFile.toString() + ")";
        this.yamlData = loadYamlFile(yamlFile);
    }
    
    /**
     * Creates a new YamlSource with pre-loaded YAML data (for testing).
     * 
     * @param yamlData the YAML data as a Map
     * @param sourceName the name of this source
     */
    public YamlSource(Map<String, Object> yamlData, String sourceName) {
        this.yamlData = yamlData != null ? yamlData : Collections.emptyMap();
        this.sourceName = sourceName;
    }
    
    @Override
    public String getName() {
        return sourceName;
    }
    
    @Override
    public Optional<String> get(String key) {
        Object value = getNestedValue(yamlData, key);
        if (value == null) {
            return Optional.empty();
        }
        
        // Convert value to string
        if (value instanceof String) {
            return Optional.of((String) value);
        } else if (value instanceof Number || value instanceof Boolean) {
            return Optional.of(String.valueOf(value));
        } else if (value instanceof List || value instanceof Map) {
            // For complex objects, return a string representation
            return Optional.of(value.toString());
        }
        
        return Optional.of(String.valueOf(value));
    }
    
    @Override
    public int getPriority() {
        return 35; // Between JSON (30) and System Properties (50)
    }
    
    /**
     * Gets a value as a specific type.
     * 
     * @param key the configuration key
     * @param type the expected type
     * @param <T> the type parameter
     * @return an Optional containing the typed value, or empty if not found or not convertible
     */
    public <T> Optional<T> getTyped(String key, Class<T> type) {
        Object value = getNestedValue(yamlData, key);
        if (value == null) {
            return Optional.empty();
        }
        
        if (type.isInstance(value)) {
            return Optional.of(type.cast(value));
        }
        
        // Handle string conversion
        if (type == String.class) {
            return Optional.of(type.cast(String.valueOf(value)));
        }
        
        // Handle number conversions
        if (value instanceof Number) {
            Number num = (Number) value;
            if (type == Integer.class) {
                return Optional.of(type.cast(num.intValue()));
            } else if (type == Long.class) {
                return Optional.of(type.cast(num.longValue()));
            } else if (type == Double.class) {
                return Optional.of(type.cast(num.doubleValue()));
            } else if (type == Float.class) {
                return Optional.of(type.cast(num.floatValue()));
            }
        }
        
        // Handle boolean conversion
        if (type == Boolean.class) {
            if (value instanceof Boolean) {
                return Optional.of(type.cast(value));
            } else if (value instanceof String) {
                String str = (String) value;
                if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                    return Optional.of(type.cast(Boolean.parseBoolean(str)));
                }
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Gets a list value from the YAML configuration.
     * 
     * @param key the configuration key
     * @return a List of objects, or empty list if not found or not a list
     */
    @SuppressWarnings("unchecked")
    public List<Object> getList(String key) {
        Object value = getNestedValue(yamlData, key);
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return Collections.emptyList();
    }
    
    /**
     * Gets a map value from the YAML configuration.
     * 
     * @param key the configuration key
     * @return a Map of string keys to objects, or empty map if not found or not a map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(String key) {
        Object value = getNestedValue(yamlData, key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return Collections.emptyMap();
    }
    
    /**
     * Gets all keys available in this YAML source.
     * 
     * @return a set of all available keys (flattened with dot notation)
     */
    public Set<String> getAllKeys() {
        Set<String> keys = new HashSet<>();
        flattenKeys(yamlData, "", keys);
        return keys;
    }
    
    /**
     * Loads and parses a YAML file.
     * 
     * @param file the YAML file to load
     * @return the parsed YAML data as a Map
     * @throws IllegalStateException if the file cannot be loaded or parsed
     */
    private Map<String, Object> loadYamlFile(Path file) {
        if (!Files.exists(file)) {
            return Collections.emptyMap(); // Graceful handling of missing files
        }
        
        try (InputStream inputStream = Files.newInputStream(file)) {
            Yaml yaml = new Yaml();
            Object data = yaml.load(inputStream);
            
            if (data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mapData = (Map<String, Object>) data;
                return mapData;
            } else {
                throw new IllegalStateException("YAML file does not contain a map structure: " + file);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load YAML file: " + file, e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse YAML file: " + file, e);
        }
    }
    
    /**
     * Retrieves a nested value using dot notation.
     * For example, "database.primary.url" will navigate through the nested structure.
     * 
     * @param data the root data map
     * @param key the dot-separated key
     * @return the value at the specified path, or null if not found
     */
    private Object getNestedValue(Map<String, Object> data, String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        
        String[] parts = key.split("\\.");
        Object current = data;
        
        for (String part : parts) {
            if (current instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> currentMap = (Map<String, Object>) current;
                current = currentMap.get(part);
            } else {
                return null;
            }
        }
        
        return current;
    }
    
    /**
     * Recursively flattens nested maps into dot-notation keys.
     * 
     * @param data the current data map
     * @param prefix the current key prefix
     * @param keys the set to add flattened keys to
     */
    private void flattenKeys(Map<String, Object> data, String prefix, Set<String> keys) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            
            if (entry.getValue() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) entry.getValue();
                flattenKeys(nestedMap, key, keys);
            } else {
                keys.add(key);
            }
        }
    }
}
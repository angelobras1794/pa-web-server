import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads configuration properties from a specified file.
 */
public class ConfigReader {
    private Properties properties;

    /**
     * Constructs a ConfigReader and loads properties from the given configuration file.
     *
     * @param configFilePath the path to the configuration file
     * @throws IOException if the configuration file is not found or cannot be read
     */
    public ConfigReader(String configFilePath) throws IOException {
        properties = new Properties();
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            throw new IOException("Configuration file not found: " + configFilePath);
        }
        try (FileInputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        }
    }

    /**
     * Retrieves the value of a specified property as a string.
     *
     * @param key the property key
     * @return the property value as a string, or null if the key does not exist
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves the value of a specified property as an integer.
     *
     * @param key the property key
     * @return the property value as an integer
     * @throws NumberFormatException if the property value is not a valid integer
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}

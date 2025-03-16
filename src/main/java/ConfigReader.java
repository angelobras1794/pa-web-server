import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

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

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}

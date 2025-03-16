import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {

    private ConfigReader configReader;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary properties file for testing
        Properties properties = new Properties();
        properties.setProperty("server.port", "8080");
        properties.setProperty("server.host", "127.0.0.1");
        properties.setProperty("server.max-conections", "5");

        try (FileOutputStream output = new FileOutputStream("test.config")) {
            properties.store(output, null);
        }

        configReader = new ConfigReader("test.config");
    }

    @Test
    void getProperty() {
        assertEquals("127.0.0.1", configReader.getProperty("server.host"));
    }

    @Test
    void getIntProperty() {
        assertEquals(8080, configReader.getIntProperty("server.port"));
    }

    @Test
    void testIOException() {
        assertThrows(IOException.class, () -> {
            new ConfigReader("non_existent_file.config");
        });
    }
}
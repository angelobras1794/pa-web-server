import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogsHandlerTest {
    private static final Path LOG_PATH = Path.of("server.log");

    @BeforeEach
    void setup() throws IOException {
        // Limpa o arquivo antes de cada teste
        Files.write(LOG_PATH, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
    }

    @AfterEach
    void tearDown() {
        LogsHandler.shutdownLogger();
    }

    @Test
    void testLogRequest() throws IOException, InterruptedException {
        // Chama o logger para registrar um pedido
        LogsHandler.logRequest("GET", "/home", "192.168.1.1", 200);

        // Espera um pouco para a escrita assíncrona terminar
        Thread.sleep(1000);

        // Lê o conteúdo do arquivo de log
        String logContent = Files.readString(LOG_PATH);

        // Verifica se contém as informações esperadas
        assertTrue(logContent.contains("\"method\":\"GET\""));
        assertTrue(logContent.contains("\"route\":\"/home\""));
        assertTrue(logContent.contains("\"origin\":\"192.168.1.1\""));
        assertTrue(logContent.contains("\"HTTP response status\":200"));
    }
}

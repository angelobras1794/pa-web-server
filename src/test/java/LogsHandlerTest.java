import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.jupiter.api.Assertions.*;

public class LogsHandlerTest {
    private LogsHandler logsHandler;
    private BlockingQueue<String> logQueue;

    @TempDir
    Path tempDir;
    private String tempLogFile;

    @BeforeEach
    void setUp() {
        tempLogFile = tempDir.resolve("logs.json").toString();
        logQueue = new LinkedBlockingQueue<>();
        logsHandler = new LogsHandler();  // Instanciando o LogsHandler, que vai gerenciar o produtor e consumidor
    }

    @Test
    void testLogRequestAddsEntryToQueue() throws InterruptedException {
        // Criar o log de entrada
        String logEntry = "  \"route\": \"/home\", \"method\": \"GET\", \"origin\": \"127.0.0.1\", \"HTTP response status\": 200, \"timestamp\": \"2025-03-26 00:00:00\",\n";

        // Criar e iniciar o produtor para adicionar o log à fila
        ProducerThread producerThread = new ProducerThread(logQueue, logEntry);
        producerThread.start();

        // Esperar a thread do produtor adicionar o log à fila
        producerThread.join();

        // Verificar se o log foi adicionado corretamente à fila
        assertFalse(logQueue.isEmpty(), "A fila de logs não deveria estar vazia após adicionar um log.");
        assertEquals(logEntry, logQueue.take(), "O log na fila não é o esperado.");
    }

    @Test
    void testInitializeFileCreatesLogFile() {
        File logFile = new File("server_root/logs/logs.json");
        assertTrue(logFile.exists(), "O arquivo de log deveria ser criado.");
    }

    @Test
    void testCloseLogsWritesClosingBracket() throws IOException {
        logsHandler.closeLogs();
        File logFile = new File("server_root/logs/logs.json");
        assertTrue(logFile.exists(), "O arquivo de log deveria existir.");

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String lastLine = "";
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            assertTrue(lastLine.trim().endsWith("}"), "O arquivo de log deveria terminar com '}'");
        }
    }
}

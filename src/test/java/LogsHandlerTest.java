import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class LogsHandlerTest {
    private static final String TEST_LOG_FILE = "src/test/resources/logs_tests.json";
    private Semaphore semaphore;
    private ReentrantLock lock;
    private LogsHandler logsHandler;

    @BeforeEach
    void setUp() throws IOException {
        semaphore = new Semaphore(0);
        lock = new ReentrantLock();
        logsHandler = new LogsHandler(semaphore, lock);
        if (!Files.exists(Path.of(TEST_LOG_FILE))) {
            Files.createFile(Path.of(TEST_LOG_FILE));
        }

    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
    }


    @Test
    void testLogRequest_AddsLogToQueue() {
        String method = "GET";
        String route = "/api/test";
        String origin = "127.0.0.1";
        int statusHttp = 200;

        LogsHandler.logRequest(method, route, origin, statusHttp);

        Queue<String> queue = LogsHandler.getLogQueue();
        assertFalse(queue.isEmpty(), "A fila de logs não deveria estar vazia após adicionar um log.");
    }

    @Test
    void testCloseLogs_AddsEOFToQueue() {
        logsHandler.closeLogs();

        Queue<String> queue = LogsHandler.getLogQueue();
        assertTrue(queue.contains("EOF"), "A fila de logs deveria conter 'EOF' após o fechamento.");
    }

    @Test
    void testRemoveClosingBracket_FileNotEmpty() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_LOG_FILE, true));
        writer.write("{\n  \"route\": \"/test\", \"method\": \"GET\", \"origin\": \"localhost\", \"HTTP response status\": 200, \"timestamp\": \"2025-03-27 10:00:00\",\n}\n");
        writer.close();

        logsHandler.closeLogs(); // Deve remover a vírgula final corretamente

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.trim().endsWith("}"), "O arquivo JSON deve terminar corretamente com '}' após o fechamento.");
    }

    @Test
    void testSemaphoreUsage() throws InterruptedException {

        LogsHandler.logRequest("POST", "/api/user", "192.168.1.1", 201);

        // teste if semaphore is 0
        assertEquals(0, semaphore.availablePermits(), "O semáforo deve ser 0 após o consumo de um log.");


    }
}

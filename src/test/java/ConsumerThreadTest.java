import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerThreadTest {
    private static final String TEST_LOG_FILE = "server_root/logs/test_logs.json";
    private Queue<String> logQueue;
    private Semaphore semaphore;
    private ReentrantLock lock;
    private ConsumerThread consumerThread;

    @BeforeEach
    void setUp() throws IOException {
        logQueue = new LinkedList<>();
        semaphore = new Semaphore(1);
        lock = new ReentrantLock();

        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
        Files.createDirectories(Paths.get("server_root/logs"));
        Files.createFile(Paths.get(TEST_LOG_FILE));

        consumerThread = new ConsumerThread(logQueue, semaphore, lock);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
    }

    @Test
    void testConsumerThread_WritesLogEntryToFile() throws IOException, InterruptedException {
        logQueue.add("Test log entry\n");
        logQueue.add("EOF");

        consumerThread.start();
        consumerThread.join(); // Aguarda a thread finalizar

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("Test log entry"), "O arquivo deve conter a entrada de log.");
    }

    @Test
    void testConsumerThread_StopsOnEOF() throws InterruptedException {
        logQueue.add("EOF");

        consumerThread.start();
        consumerThread.join();

        assertTrue(logQueue.isEmpty(), "A fila deve estar vazia após consumir 'EOF'.");
    }

    @Test
    void testSemaphoreAndLockUsage() throws InterruptedException {
        semaphore.acquire();
        lock.lock();
        logQueue.add("Log entry\n");
        lock.unlock();
        semaphore.release();

        assertEquals(1, semaphore.availablePermits(), "O semáforo deve estar disponível após o uso.");
        assertFalse(lock.isLocked(), "O lock deve estar desbloqueado.");
    }
}

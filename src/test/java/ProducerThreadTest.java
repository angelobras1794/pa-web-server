import org.junit.jupiter.api.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.jupiter.api.Assertions.*;

public class ProducerThreadTest {
    private BlockingQueue<String> logQueue;
    private ProducerThread producerThread;

    @BeforeEach
    void setUp() {
        logQueue = new LinkedBlockingQueue<>();
    }

    @Test
    void testProducerAddsLogEntryToQueue() throws InterruptedException {
        String logEntry = "Test log entry";
        producerThread = new ProducerThread(logQueue, logEntry);

        producerThread.start();
        producerThread.join();

        assertEquals(logEntry, logQueue.take());
    }

    @Test
    void testProducerHandlesInterruptionsGracefully() {
        String logEntry = "Interrupted log entry";
        producerThread = new ProducerThread(logQueue, logEntry);

        producerThread.start();
        producerThread.interrupt();

        assertDoesNotThrow(() -> producerThread.join());
    }
}
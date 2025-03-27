import org.junit.jupiter.api.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class ProducerThreadTest {
    private Queue<String> logQueue;
    private Semaphore semaphore;
    private ReentrantLock lock;
    private ProducerThread producerThread;

    @BeforeEach
    void setUp() {
        logQueue = new LinkedList<>();
        semaphore = new Semaphore(0); // Começa bloqueado
        lock = new ReentrantLock();
    }

    @Test
    void testProducerThread_AddsLogToQueue() throws InterruptedException {
        String logEntry = "Test log entry";

        producerThread = new ProducerThread(logQueue, logEntry, semaphore, lock);
        producerThread.start();
        producerThread.join();

        assertEquals(1, logQueue.size(), "A fila deve conter um log.");
        assertEquals(logEntry, logQueue.peek(), "O log na fila deve ser o esperado.");
    }

    @Test
    void testProducerThread_ReleasesSemaphore() throws InterruptedException {
        producerThread = new ProducerThread(logQueue, "Log entry", semaphore, lock);
        producerThread.start();
        producerThread.join();

        assertEquals(1, semaphore.availablePermits(), "O semáforo deve ser liberado após adicionar um log.");
    }

    @Test
    void testProducerThread_UsesLockCorrectly() throws InterruptedException {
        producerThread = new ProducerThread(logQueue, "Log entry", semaphore, lock);
        producerThread.start();
        producerThread.join();

        assertFalse(lock.isLocked(), "O lock deve estar desbloqueado após a execução do producer.");
    }
}

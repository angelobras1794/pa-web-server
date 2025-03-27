import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The ProducerThread class represents a producer thread that generates log entries
 * and adds them to a shared log queue.
 * This class is designed to be used in a multi-threaded environment where multiple
 * producers can concurrently add log entries to the shared queue, ensuring thread safety
 * using a semaphore and a lock.
 */
public class ProducerThread extends Thread {

    // The queue where log entries will be added
    private final Queue<String> logQueue;

    // The log entry to be added to the queue
    private final String logEntry;

    // Semaphore to control access to the log queue
    private final Semaphore semaphore;

    // Lock to synchronize access to the log queue
    private final ReentrantLock lock;

    /**
     * Constructs a ProducerThread that will add a log entry to the provided log queue.
     * The producer will use the provided semaphore and lock to synchronize access to the queue.
     *
     * @param logQueue The queue to which the producer will add log entries.
     * @param logEntry The log entry to be added to the queue.
     * @param semaphore The semaphore used to control access to the log queue.
     * @param lock The lock used to synchronize access to the log queue.
     */
    public ProducerThread(Queue<String> logQueue, String logEntry, Semaphore semaphore, ReentrantLock lock) {
        this.logQueue = logQueue;
        this.logEntry = logEntry;
        this.semaphore = semaphore;
        this.lock = new ReentrantLock();
    }

    /**
     * The run method of the producer thread. This method adds a log entry to the shared log queue.
     * It acquires the lock to ensure thread safety while adding the entry, and releases the semaphore
     * to notify any waiting consumers that a new log entry has been added.
     */
    @Override
    public void run() {
        try {
            // Lock access to the queue for thread safety
            lock.lock();

            // Add the log entry to the queue
            logQueue.add(logEntry);

            // Release the semaphore to allow a consumer to process the log entry
            semaphore.release();

            // Unlock the queue access
            lock.unlock();
        } catch (Exception e) {
            // Interrupt the thread if an exception occurs
            Thread.currentThread().interrupt();
        }
    }
}

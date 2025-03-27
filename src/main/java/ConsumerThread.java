import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The ConsumerThread class represents a consumer thread that processes log entries from a queue
 * and writes them to a log file.
 * This class is designed to be used in a multi-threaded environment where multiple consumers can
 * consume log entries concurrently from a shared queue, ensuring thread safety using a semaphore and a lock.
 */
public class ConsumerThread extends Thread {

    // The queue from which log entries are consumed
    private final Queue<String> logQueue;

    // Path to the log file where the logs will be written
    private static final String LOG_FILE = "server_root/logs/logs.json";

    // Semaphore to control access to the log queue
    private final Semaphore semaphore;

    // Lock to synchronize access to the log queue
    private final ReentrantLock lock;

    /**
     * Constructs a ConsumerThread that will process log entries from the provided log queue.
     * The consumer will use the provided semaphore and lock to synchronize access to the queue.
     *
     * @param logQueue The queue from which the consumer will consume log entries.
     * @param semaphore The semaphore used to control access to the log queue.
     * @param lock The lock used to synchronize access to the log queue.
     */
    public ConsumerThread(Queue<String> logQueue, Semaphore semaphore, ReentrantLock lock) {
        this.logQueue = logQueue;
        this.semaphore = semaphore;
        this.lock = lock;
    }

    /**
     * The run method of the consumer thread. This method continuously consumes log entries from the queue
     * and writes them to the log file. It will stop when it encounters an "EOF" log entry, which is used as a signal to terminate.
     * The thread acquires the semaphore to ensure thread-safe access to the queue and locks access to the queue
     * using a ReentrantLock to ensure that only one thread can access the queue at a time.
     */
    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            // Infinite loop to keep consuming logs until the "EOF" entry is encountered
            while (true) {
                // Acquire the semaphore to safely access the log queue
                semaphore.acquireUninterruptibly();

                // Lock access to the queue for thread safety
                lock.lock();

                // Poll the log queue for the next log entry
                String logEntry = logQueue.poll();

                // Check if the log entry is "EOF", which indicates the end of the processing
                if (logEntry != null && logEntry.equals("EOF")) {
                    break; // Exit the loop if EOF is encountered
                }

                // Unlock the queue access
                lock.unlock();

                // Write the log entry to the log file
                if (logEntry != null) {
                    writer.write(logEntry);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            // Interrupt the thread if an IOException occurs
            Thread.currentThread().interrupt();
        }
    }
}

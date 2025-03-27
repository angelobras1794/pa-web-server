import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The LogsHandler class manages logging functionality by writing log entries to a JSON file.
 * It handles the initialization of the log file, log entry writing, and managing multiple consumer threads to process log entries.
 * This class ensures that logs are written in a thread-safe manner using a semaphore and a reentrant lock.
 */
public class LogsHandler {

    // Path to the log file
    private static final String LOG_FILE = "server_root/logs/logs.json";

    // Number of consumer threads
    private static final int NUM_CONSUMERS = 1;

    // Queue to store log entries
    private static final Queue<String> logQueue = new LinkedList<>();

    // Flag to check if the log has already been closed
    private static boolean closed = false;

    // Semaphore to control access to the log
    private static Semaphore semaphore = null;

    // Lock to synchronize access to the log queue
    private static ReentrantLock lock = null;

    /**
     * Constructor for the LogsHandler class.
     * Initializes the semaphore and lock, and starts the logging process by calling the appropriate methods.
     *
     * @param semaphore The semaphore used to control access to the log.
     * @param lock The lock used to synchronize access to the log queue.
     */
    public LogsHandler(Semaphore semaphore, ReentrantLock lock) {
        this.semaphore = semaphore;
        this.lock = lock;
        beforeExecute();
        execute();
        afterExecute();
    }

    /**
     * Performs initialization tasks before starting log processing.
     * Specifically, it initializes the log file.
     */
    private void beforeExecute() {
        initializeFile();
    }

    /**
     * Starts the consumer threads that will process the log entries from the queue.
     */
    private void execute() {
        startConsumers();
    }

    /**
     * Registers a shutdown hook to close the logs when the application terminates.
     */
    private void afterExecute() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeLogs));
    }

    /**
     * Initializes the log file by creating it if it doesn't exist or cleaning up the closing bracket if necessary.
     */
    private void initializeFile() {
        try {
            File file = new File(LOG_FILE);
            if (!file.exists() || file.length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
                    writer.write("{\n");
                    writer.flush();
                }
            } else {
                removeClosingBracket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs an HTTP request by creating a log entry and adding it to the queue.
     * This method will be called to log information such as the HTTP method, route, origin, status, and timestamp.
     * A new producer thread is created to handle the addition of the log entry to the queue.
     *
     * @param method The HTTP method (e.g., GET, POST).
     * @param route The route of the HTTP request.
     * @param origin The origin of the request.
     * @param statusHttp The HTTP status code of the response.
     */
    public static void logRequest(String method, String route, String origin, int statusHttp) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format(
                "  \"route\": \"%s\", \"method\": \"%s\", \"origin\": \"%s\", \"HTTP response status\": %d, \"timestamp\": \"%s\",\n",
                route, method, origin, statusHttp, timestamp
        );

        new ProducerThread(logQueue, logEntry, semaphore, lock).start();
    }

    /**
     * Starts the consumer threads that will process log entries from the queue.
     * The number of consumer threads is controlled by the NUM_CONSUMERS constant.
     */
    private void startConsumers() {
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            new ConsumerThread(logQueue, semaphore, lock).start();
        }
    }

    /**
     * Removes the last closing bracket from the log file to maintain proper JSON formatting.
     * This method is called if the log file already exists to ensure it is not malformed.
     */
    private static void removeClosingBracket() {
        try (RandomAccessFile raf = new RandomAccessFile(LOG_FILE, "rw")) {
            long length = raf.length();

            if (length < 3) {
                return;
            }

            raf.seek(length - 3);
            byte lastByte = raf.readByte();
            if (lastByte == ',') {
                raf.setLength(length - 3);
            } else {
                raf.setLength(length - 2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the log file by writing a closing bracket to indicate the end of the JSON log entries.
     * It also notifies the consumers to stop processing by adding an "EOF" entry to the queue.
     */
    public void closeLogs() {
        if (!closed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write("  \n}\n");
                writer.flush();
                closed = true;

                for (int i = 0; i < NUM_CONSUMERS; i++) {
                    logQueue.add("EOF");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the log queue. This is useful for testing and inspecting the contents of the queue.
     *
     * @return The log queue.
     */
    public static Queue<String> getLogQueue() {
        return logQueue;
    }
}

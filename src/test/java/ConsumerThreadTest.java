import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumerThreadTest {
    private BlockingQueue<String> logQueue;
    private ConsumerThread consumerThread;

    @TempDir
    Path tempDir;
    private String tempLogFile;

    @BeforeEach
    void setUp() {
        logQueue = new LinkedBlockingQueue<>();
        tempLogFile = tempDir.resolve("logs.json").toString();
        consumerThread = new ConsumerThread(logQueue) {
            @Override
            public void run() {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempLogFile, true))) {
                    while (true) {
                        String logEntry = logQueue.poll(5, TimeUnit.SECONDS);
                        if (logEntry == null) continue;
                        if (logEntry.equals("EOF")) break;
                        writer.write(logEntry);
                        writer.flush();
                    }
                } catch (IOException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    @Test
    void testConsumerWritesLogToFile() throws InterruptedException, IOException {
        String logEntry = "Test log entry\n";
        logQueue.put(logEntry);
        logQueue.put("EOF");

        consumerThread.start();
        consumerThread.join();

        File logFile = new File(tempLogFile);
        assertTrue(logFile.exists());

        String content;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            content = reader.readLine();
        }
        assertEquals(logEntry.trim(), content.trim());
    }

    @Test
    void testConsumerStopsOnEOF() throws InterruptedException {
        logQueue.put("EOF");
        consumerThread.start();
        consumerThread.join();
        assertTrue(logQueue.isEmpty());
    }
}

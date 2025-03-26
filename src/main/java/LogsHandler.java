import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogsHandler {
    private static final String LOG_FILE = "server_root/logs/logs.json";
    private static final int NUM_CONSUMERS = 2;
    private static final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private static boolean closed = false;

    public LogsHandler() {
        beforeExecute();
        execute();
        afterExecute();
    }

    private void beforeExecute() {
        initializeFile();
    }

    private void execute() {
        startConsumers();
    }

    private void afterExecute() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeLogs));
    }

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

    public static void logRequest(String method, String route, String origin, int statusHttp) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format(
                "  \"route\": \"%s\", \"method\": \"%s\", \"origin\": \"%s\", \"HTTP response status\": %d, \"timestamp\": \"%s\",\n",
                route, method, origin, statusHttp, timestamp
        );

        new ProducerThread(logQueue, logEntry).start();
    }

    private void startConsumers() {
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            new ConsumerThread(logQueue).start();
        }
    }

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

    public void closeLogs() {
        if (!closed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write("  \n}\n");
                writer.flush();
                closed = true;

                for (int i = 0; i < NUM_CONSUMERS; i++) {
                    logQueue.put("EOF");
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static BlockingQueue<String> getLogQueue() {
        return logQueue;
    }
}

import java.util.concurrent.BlockingQueue;

public class ProducerThread extends Thread {
    private final BlockingQueue<String> logQueue;
    private final String logEntry;

    public ProducerThread(BlockingQueue<String> logQueue, String logEntry) {
        this.logQueue = logQueue;
        this.logEntry = logEntry;
    }

    @Override
    public void run() {
        try {
            logQueue.put(logEntry);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerThread extends Thread {
    private final Queue<String> logQueue;
    private final String logEntry;
    private final Semaphore semaphore;
    private final ReentrantLock lock;

    public ProducerThread(Queue<String> logQueue, String logEntry, Semaphore semaphore, ReentrantLock lock) {
        this.logQueue = logQueue;
        this.logEntry = logEntry;
        this.semaphore = semaphore;
        this.lock = new ReentrantLock();
    }


    @Override
    public void run() {
        try {
            lock.lock();

            logQueue.add(logEntry);

            semaphore.release();
            lock.unlock();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}

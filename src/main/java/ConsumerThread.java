import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerThread extends Thread {
    private final Queue<String> logQueue;
    private static final String LOG_FILE = "server_root/logs/logs.json";
    private final Semaphore semaphore;
    private final ReentrantLock lock;

    public ConsumerThread(Queue<String> logQueue, Semaphore semaphore, ReentrantLock lock) {

        this.logQueue = logQueue;
        this.semaphore = semaphore;
        this.lock = lock;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            while (true) {
                semaphore.acquireUninterruptibly();
                lock.lock();
                String logEntry = logQueue.poll();


                // Verifica se é o "poison pill" para encerrar
                if (logEntry.equals("EOF")) break;
                lock.unlock();
                writer.write(logEntry);
                writer.flush();
            }
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
    }
}

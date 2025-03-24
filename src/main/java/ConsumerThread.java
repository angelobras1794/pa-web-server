import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConsumerThread extends Thread {
    private final BlockingQueue<String> logQueue;
    private static final String LOG_FILE = "server_root/logs/logs.json";

    public ConsumerThread(BlockingQueue<String> logQueue) {
        this.logQueue = logQueue;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            while (true) {
                String logEntry = logQueue.poll(5, TimeUnit.SECONDS); // Espera até 5s por um log

                if (logEntry == null) continue; // Evita escrita vazia

                // Verifica se é o "poison pill" para encerrar
                if (logEntry.equals("EOF")) break;

                writer.write(logEntry);
                writer.flush();
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

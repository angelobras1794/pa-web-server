
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LogsHandler {

    private static final String LOG_FILE = "server.log";
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void logRequest(String method, String route, String origin, int statusHttp) {
        executor.submit(() -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject logs = new JSONObject();

            logs.put("timestamp", timestamp);
            logs.put("method", method);
            logs.put("route", route);
            logs.put("origin", origin);
            logs.put("HTTP response status", statusHttp);

            writeLogs(logs.toString());

        });
    }

    public static void writeLogs(String logs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logs);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void shutdownLogger() {
        executor.shutdown();
    }

}

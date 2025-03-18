
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LogsHandler {

    private static String logFile = "";

    public LogsHandler(String filename) {
        logFile = filename;
    }


    public static void logRequest(String method, String route, String origin, int statusHttp) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JSONObject logs = new JSONObject();

            logs.put("timestamp", timestamp);
            logs.put("method", method);
            logs.put("route", route);
            logs.put("origin", origin);
            logs.put("HTTP response status", statusHttp);

            //print logs
            System.out.println(logs);

            writeLogs(logs.toString());

    }

    public static void writeLogs(String logs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logs);

            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

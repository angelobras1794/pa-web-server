import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogsHandler {

    private static String logFile = "";
    private static boolean closed = false; // Para evitar reescrita de }

    public LogsHandler(String filename) {
        logFile = filename;

        // garantir que "}" seja escrita ao fechar o programa
        Runtime.getRuntime().addShutdownHook(new Thread(LogsHandler::closeLogs));

        // Garante que o arquivo começa corretamente
        initializeFile();
    }

    private void initializeFile() {
        try {
            File file = new File(logFile);
            if (!file.exists() || file.length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
                    writer.write("{\n"); // Cria o arquivo com '{' apenas se estiver vazio
                    writer.flush();
                }
            } else {
                // Verifica se já tem uma "}" no final e remove para adicionar novos logs
                removeClosingBracket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logRequest(String method, String route, String origin, int statusHttp) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder logEntry = new StringBuilder();

        logEntry.append("\"route\": \"").append(route).append("\", ")
                .append("\"method\": \"").append(method).append("\", ")
                .append("\"origin\": \"").append(origin).append("\", ")
                .append("\"HTTP response status\": ").append(statusHttp).append(", ")
                .append("\"timestamp\": \"").append(timestamp).append("\"\n");

        writeLogs(logEntry.toString());
    }

    private static void writeLogs(String logEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logEntry);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeClosingBracket() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(logFile));
            if (!lines.isEmpty() && lines.get(lines.size() - 1).trim().equals("}")) {
                lines.remove(lines.size() - 1); // Remove a última linha (a chave '}')
                Files.write(Paths.get(logFile), lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeLogs() {
        if (!closed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write("}");
                writer.newLine();
                writer.flush();
                closed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

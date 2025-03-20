import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RequestThread implements Runnable {
    private Socket client;
    private final String SERVER_ROOT;
    private final LogsHandler logsHandler;

    public RequestThread(Socket client, String SERVER_ROOT, LogsHandler logsHandler) {
        this.client = client;
        this.SERVER_ROOT = SERVER_ROOT;
        this.logsHandler = logsHandler;

    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             OutputStream clientOutput = client.getOutputStream()) {

            System.out.println("New client connected: " + client);
                System.out.println("Still connected? " + !client.isClosed());
                StringBuilder requestBuilder = new StringBuilder();
                String line;
                while (!(line = br.readLine()).isBlank()) {
                    requestBuilder.append(line).append("\r\n");
                }
                // If no request is received, assume the client has closed the connection
                if (requestBuilder.isEmpty()) {
                    System.out.println("Client disconnected: " + client);
                    return;
                }

                String request = requestBuilder.toString();
                String[] tokens = request.split(" ");
                if (tokens.length < 2) {
                    System.err.println("Invalid request received.");
                    return;
                }
                String route = tokens[1];
                System.out.println("Request received: " + request);
                System.out.println("Route received: " + route);

                // Ignore favicon requests
                if ("/favicon.ico".equals(route)) {
                    System.out.println("Ignoring favicon request");
                    return;
                }


                RequestHandler requestHandler = new RequestHandler(route, SERVER_ROOT);
                requestHandler.handleRequest();
                String httpUrl = requestHandler.getHttpUrl();
                System.out.println(httpUrl);

                byte[] content = readBinaryFile(httpUrl);

                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write("Content-Type: text/html\r\n".getBytes());
                clientOutput.write("Connection: keep-alive\r\n".getBytes());
                clientOutput.write("Content-Length: ".getBytes());
                clientOutput.write(String.valueOf(content.length).getBytes());

                clientOutput.write("\r\n".getBytes());
                 // Keep connection open
                clientOutput.write("\r\n".getBytes());

                clientOutput.write(content);
                clientOutput.flush();

                if (requestHandler.isError404()) {
                    logsHandler.logRequest(tokens[0], route, client.getInetAddress().getHostAddress(), 404);
                } else {
                    logsHandler.logRequest(tokens[0], route, client.getInetAddress().getHostAddress(), 200);
                }
                System.out.println("Still connected2? " + !client.isClosed());


        } catch (IOException e) {
            System.err.println("Error handling client request.");
            e.printStackTrace();
        }


    }
    /**
     * Reads a binary file and returns its contents as a byte array.
     *
     * @param path The file path to read.
     * @return A byte array containing the file's contents, or an empty array if an error occurs.
     */
    private byte[] readBinaryFile(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            System.err.println("Error reading file: " + path);
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * Reads a text file and returns its contents as a string.
     *
     * @param path The file path to read.
     * @return A string containing the file's contents, or an empty string if an error occurs.
     */
    private String readFile(String path) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + path);
            e.printStackTrace();
        }
        return content.toString();
    }
}
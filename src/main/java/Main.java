import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) throws IOException {
        ConfigReader configReader = new ConfigReader("server_root/config/server.config");
        int port = configReader.getIntProperty("server.port");
        String host = configReader.getProperty("server.host");
        int max_connections = configReader.getIntProperty("server.max-conections");
        MainHTTPServerThread s = new MainHTTPServerThread(port, host, max_connections, "server_root/logs/logs.json");
        s.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Create a thread pool to simulate multiple clients
//        ExecutorService clientPool = Executors.newFixedThreadPool(10);
//
//        // Send 10 requests concurrently
//        for (int i = 0; i < 60; i++) {
//            clientPool.submit(() -> {
//                try {
//                    URL url = new URL("http://localhost:8080/user/caalhos/ola.html");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//
//                    int responseCode = connection.getResponseCode();
//                    System.out.println("Response Code: " + responseCode);
//
//                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String inputLine;
//                    StringBuilder response = new StringBuilder();
//
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//
//                    System.out.println("Response: " + response.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//
//        }
//
//    }
    }
}

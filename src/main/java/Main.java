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




    }
}

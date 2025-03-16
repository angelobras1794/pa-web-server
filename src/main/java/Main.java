import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws IOException {
        ConfigReader configReader = new ConfigReader("server_root/config/server.config");
        int port = configReader.getIntProperty("server.port");
        String host = configReader.getProperty("server.host");
        int max_connections = configReader.getIntProperty("server.max-conections");
        MainHTTPServerThread s = new MainHTTPServerThread(port,host,max_connections);
        s.start();
        try {
            s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}

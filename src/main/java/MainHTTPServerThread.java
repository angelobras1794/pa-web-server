import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple HTTP server that listens on a specified port.
 * It serves files from a predefined server root directory.
 */
public class MainHTTPServerThread extends Thread {

    private static final String SERVER_ROOT = "server_root"; // Define by user
    private final int port;
    private final String host;
    private final int max_connections;
    private ServerSocket server;
    private LogsHandler logsHandler;
    private ThreadPool threadPool;
    private PageHandler pageHandler;
    private Semaphore semaphore;
    private ReentrantLock lock;

    /**
     * Constructor to initialize the HTTP server thread with a specified port, host, maximum connections, and log file.
     *
     * @param port The port number on which the server will listen.
     * @param host The host address on which the server will listen.
     * @param max_connections The maximum number of concurrent connections.
     * @param logFile The file path for logging requests.
     */
    public MainHTTPServerThread(int port, String host, int max_connections, String logFile) {
        this.port = port;
        this.host = host;
        this.max_connections = max_connections;
        semaphore = new Semaphore(0);
        lock = new ReentrantLock();


        logsHandler = new LogsHandler(semaphore,lock);
        threadPool = new ThreadPool(max_connections, max_connections, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        pageHandler = new PageHandler();
    }


    /**
     * Starts the HTTP server and listens for incoming client requests.
     * Processes HTTP GET requests and serves files from the defined server root directory.
     */
    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
            System.out.println("Working Directory: " + System.getProperty("user.dir"));

            while (true) {
                Socket client = server.accept();
                threadPool.execute(new RequestThread(client, SERVER_ROOT, logsHandler, pageHandler));
            }
        } catch (IOException e) {
            System.err.println("Server error: Unable to start on port " + port);
            e.printStackTrace();
        }
    }
}
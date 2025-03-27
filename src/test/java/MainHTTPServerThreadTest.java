import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class MainHTTPServerThreadTest {
    private MainHTTPServerThread serverThread;
    private Thread server;
    private int port = 8090;

    @BeforeEach
    void setUp() {
        serverThread = new MainHTTPServerThread(port, "localhost", 10);
        server = new Thread(serverThread);
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        serverThread.interrupt();
        serverThread = null;
    }

    @Test
    void testServerStartsSuccessfully() {
        try (Socket clientSocket = new Socket("localhost", port)) {
            assertTrue(clientSocket.isConnected());
        } catch (IOException e) {
            fail("Failed to connect to the server");
        }
    }

    @Test
    void testServerHandlesClientConnection() {
        try (Socket clientSocket = new Socket("localhost", port);
             OutputStream out = clientSocket.getOutputStream()) {
            String request = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
            out.write(request.getBytes());
            out.flush();
            assertTrue(clientSocket.isConnected());
        } catch (IOException e) {
            fail("Failed to connect to the server or send request");
        }
    }

    @Test
    void testServerFailsToStart() {
        try (ServerSocket conflictingServer = new ServerSocket(port)) {
            MainHTTPServerThread conflictingServerThread = new MainHTTPServerThread(port, "localhost", 10);
            Thread conflictingThread = new Thread(conflictingServerThread);
            conflictingThread.start();
            fail("Server should not start on the same port");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Address already in use"));
        }
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {
    private RequestHandler requestHandler;
    private final String serverRoot = "src/test/resources/";

    @BeforeEach
    void setUp() {
        requestHandler = new RequestHandler("test.html", serverRoot);
    }

    @Test
    void testHtmlSearcher_FileExists() {
        Path expectedPath = Paths.get(serverRoot, "test.html");
        System.out.println(expectedPath);
        Path actualPath = requestHandler.htmlSearcher(serverRoot, "test.html");
        System.out.println(actualPath);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testHtmlSearcher_FileDoesNotExist() {
        requestHandler = new RequestHandler("nonexistent.html", serverRoot);
        Path actualPath = requestHandler.htmlSearcher(serverRoot, "nonexistent.html");
        assertNull(actualPath);
    }

    @Test
    void testHandleRequest_FileExists() {
        requestHandler.handleRequest();
        assertEquals(Paths.get(serverRoot, "test.html").toString(), requestHandler.getHttpUrl());
    }

    @Test
    void testHandleRequest_FileDoesNotExist() {
        requestHandler = new RequestHandler("nonexistent.html", serverRoot);
        requestHandler.handleRequest();
        assertEquals("html/404.html", requestHandler.getHttpUrl());
    }

    @Test
    void testHandleRequest_NoFileSpecified() {
        requestHandler = new RequestHandler("", serverRoot);
        requestHandler.handleRequest();
        assertTrue(requestHandler.getHttpUrl().contains("index.html"));
    }

    @Test
    void testHandleRequest_IconFile() {
        requestHandler = new RequestHandler("favicon.ico", serverRoot);
        requestHandler.handleRequest();
        assertFalse(requestHandler.isError404());
    }
}
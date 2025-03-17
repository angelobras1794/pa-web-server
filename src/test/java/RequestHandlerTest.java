
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class RequestHandlerTest {


    private RequestHandler requestHandler;
    private final String serverRoot = "src/test/resources";

    @BeforeEach
    public void setUp() {
        requestHandler = new RequestHandler("test.html", serverRoot);
    }

    @Test
    public void testHtmlSearcher_FileExists() {
        Path expectedPath = Paths.get(serverRoot, "test.html");
        Path actualPath = requestHandler.htmlSearcher(serverRoot, "test.html");
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testHtmlSearcher_FileDoesNotExist() {
        requestHandler = new RequestHandler("nonexistent.html", serverRoot);
        Path actualPath = requestHandler.htmlSearcher(serverRoot, "nonexistent.html");
        assertNull(actualPath);
    }


    @Test
    public void testHandleRequest_FileExists() {
        requestHandler.handleRequest();
        assertEquals(Paths.get(serverRoot, "test.html").toString(), requestHandler.getHttpUrl());
    }

    @Test
    public void testHandleRequest_FileDoesNotExist() {
        requestHandler = new RequestHandler("/files/", serverRoot);
        requestHandler.handleRequest();
        assertEquals("html/404.html", requestHandler.getHttpUrl());
    }

    @Test
    public void testHandleRequest_NoFileSpecified() {
        requestHandler = new RequestHandler("", serverRoot);
        requestHandler.handleRequest();
        assertTrue(requestHandler.getHttpUrl().contains("index.html"));
    }

 }

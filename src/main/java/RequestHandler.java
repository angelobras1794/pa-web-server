import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Handles HTTP requests for HTML files within a specified server root directory.
 */
public class RequestHandler {
    private final String htmlFileName;
    private final String serverRoot;
    private String httpUrl;
    private boolean error404;

    /**
     * Gets the resolved HTTP URL for the requested file.
     *
     * @return the HTTP URL as a String
     */
    public String getHttpUrl() {
        return httpUrl;
    }

    /**
     * Constructs a RequestHandler with the specified HTML file name and server root.
     *
     * @param htmlFileName the name of the requested HTML file
     * @param serverRoot   the root directory of the server
     */
    public RequestHandler(String htmlFileName, String serverRoot) {
        this.htmlFileName = htmlFileName;
        this.serverRoot = serverRoot;
    }

    /**
     * Handles the request by searching for the requested HTML file within the server root.
     * If the file is not found, it sets the URL to a 404 error page.
     */
    public void handleRequest() {
        if (htmlFileName.endsWith(".html")) {
            Path foundPath = htmlSearcher(serverRoot, htmlFileName);
            if (foundPath != null) {
                httpUrl = foundPath.toString();
                error404 = false;
            } else {
                httpUrl = "html/404.html";
                error404 = true;
            }
        } else if (htmlFileName.endsWith(".ico")) {
            error404 = false;
        } else {
            String newPath = serverRoot + htmlFileName;
            System.out.println("Novo path" + newPath);
            Path indexPath = htmlSearcher(newPath, "index.html");
            if (indexPath == null) {
                httpUrl = "html/404.html";
                error404 = true;
            } else {
                httpUrl = indexPath.toString();
                error404 = false;
            }
        }
    }

    /**
     * Searches for an HTML file within the given server root directory.
     *
     * @param serverRoot   the root directory where the search starts
     * @param htmlFileName the name of the HTML file to search for
     * @return the Path to the found file, or null if not found
     */
    public Path htmlSearcher(String serverRoot, String htmlFileName) {
        Path startDir = Paths.get(serverRoot);
        AtomicReference<Path> foundPath = new AtomicReference<>(null);
        try {
            Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    System.out.println("Checking file: " + file);
                    System.out.println("htmlFileName: " + serverRoot + htmlFileName);
                    if (file.toString().equals(Paths.get(serverRoot + htmlFileName).toString())) {
                        System.out.println("htmlFileName: " + htmlFileName);
                        System.out.println("Found file: " + file);
                        foundPath.set(file);
                        return FileVisitResult.TERMINATE; // Stop searching
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundPath.get(); // Return found file path or null
    }

    /**
     * Checks if the request resulted in a 404 error.
     *
     * @return true if the requested file was not found, false otherwise
     */
    public boolean isError404() {
        return error404;
    }
}

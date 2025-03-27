import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RequestHandler {
    private final String htmlFileName;
    private final String serverRoot;
    private String httpUrl;
    private boolean error404;

    public String getHttpUrl() {
        return httpUrl;
    }


    public RequestHandler(String htmlFileName, String serverRoot) {
        this.htmlFileName = htmlFileName;
        this.serverRoot = serverRoot;

    }

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
    public boolean isError404() {return error404;}


}



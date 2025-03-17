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
    private  String httpUrl ="";

    public String getHttpUrl() {
        return httpUrl;
    }



    public RequestHandler(String htmlFileName,String serverRoot){
        this.htmlFileName = htmlFileName;
        this.serverRoot = serverRoot;

    }

    public void handleRequest() {
        if (!htmlFileName.isBlank()) {
            Path foundPath = htmlSearcher();
            if (foundPath != null) {
                httpUrl = foundPath.toString();
            } else {
                httpUrl = "html/404.html";
            }
        } else {
            List<Path> indexFilesPath = findAllFiles();
            if (indexFilesPath.isEmpty()) {
                httpUrl = "html/404.html";
            } else {
                for (Path p : indexFilesPath) {
                    httpUrl += p.toString() + "\n";
                }
            }
        }
    }
    public boolean checkHtmlFile(){
        return htmlSearcher() != null && htmlFileName.endsWith(".html");
    }



    public Path htmlSearcher(){
        Path startDir = Paths.get(serverRoot);
        AtomicReference<Path> foundPath = new AtomicReference<>(null);
        try {
            Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().equals(htmlFileName)) {
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
    public List<Path> findAllFiles(){
            Path startDir = Paths.get(serverRoot);
            List<Path> foundFiles = new ArrayList<>();

            try {
                Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (file.getFileName().toString().equals("index.html")) {
                            foundFiles.add(file);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return foundFiles; // Return list of file paths
        }


    }



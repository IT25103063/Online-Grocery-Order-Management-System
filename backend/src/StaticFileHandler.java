import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * StaticFileHandler
 * Serves static assets (HTML, CSS, JS) from a target directory.
 * Maps URL paths to local file paths and resolves MIME types dynamically.
 */
public class StaticFileHandler implements HttpHandler {

    private final String baseDir;

    public StaticFileHandler(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Only allow GET requests for static files
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            return;
        }

        String path = exchange.getRequestURI().getPath();

        // Default root path "/" to "/index.html"
        if (path.equals("/")) {
            path = "/index.html";
        }

        // Map URI path to local filesystem path
        File file = new File(baseDir, path);

        // Security Check: Prevent path traversal (e.g., /../data/suppliers.txt)
        String canonicalBase = new File(baseDir).getCanonicalPath();
        String canonicalFile = file.getCanonicalPath();
        if (!canonicalFile.startsWith(canonicalBase)) {
            sendError(exchange, 403, "403 Forbidden: Access Denied");
            return;
        }

        // Verify file existence and readability
        if (!file.exists() || file.isDirectory()) {
            sendError(exchange, 404, "404 Not Found: The resource you requested does not exist");
            return;
        }

        // Set appropriate MIME Content-Type
        String contentType = getContentType(file.getName());
        exchange.getResponseHeaders().set("Content-Type", contentType);

        // Serve CORS headers just in case
        CORSHandler.addCorsHeaders(exchange);

        // Send 200 OK with file length
        long fileLength = file.length();
        exchange.sendResponseHeaders(200, fileLength);

        // Stream file contents to browser client
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = exchange.getResponseBody()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    private String getContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=utf-8";
        if (lower.endsWith(".css")) return "text/css; charset=utf-8";
        if (lower.endsWith(".js"))  return "application/javascript; charset=utf-8";
        if (lower.endsWith(".json")) return "application/json; charset=utf-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }

    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        byte[] bytes = message.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        CORSHandler.addCorsHeaders(exchange);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

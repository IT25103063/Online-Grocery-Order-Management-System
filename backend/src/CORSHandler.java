import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class CORSHandler {

    /**
     * Adds CORS headers to every outgoing response.
     * Call this before sending any response.
     */
    public static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    /**
     * Handles an OPTIONS preflight request.
     * Sends 204 No Content with CORS headers and closes the exchange.
     * Returns true if this was an OPTIONS request (so the caller can return early).
     */
    public static boolean handlePreflight(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return true;
        }
        return false;
    }
}

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * ProductHandler
 * Handles REST routing for product endpoints:
 *   GET  /api/products?supplierId=SUP-001
 *   POST /api/products
 */
public class ProductHandler implements HttpHandler {

    private final ProductFileStorage storage;

    public ProductHandler(ProductFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // 1. CORS Preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            CORSHandler.addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            if ("GET".equalsIgnoreCase(method)) {
                handleGet(exchange);
            } else if ("POST".equalsIgnoreCase(method)) {
                handlePost(exchange);
            } else {
                sendResponse(exchange, 451, "405 Method Not Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "500 Internal Server Error: " + e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String supplierId = null;

        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1 && "supplierId".equalsIgnoreCase(pair[0])) {
                    supplierId = pair[1].trim();
                }
            }
        }

        List<Product> products;
        if (supplierId != null && !supplierId.isEmpty()) {
            products = storage.getBySupplier(supplierId);
        } else {
            products = storage.getAll();
        }

        // Convert list to JSON array
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            json.append(String.format(
                "{\"productId\":\"%s\",\"supplierId\":\"%s\",\"productName\":\"%s\",\"category\":\"%s\",\"unitPrice\":%d,\"stockCount\":%d}",
                escape(p.getProductId()),
                escape(p.getSupplierId()),
                escape(p.getProductName()),
                escape(p.getCategory()),
                p.getUnitPrice(),
                p.getStockCount()
            ));
            if (i < products.size() - 1) json.append(",");
        }
        json.append("]");

        sendJsonResponse(exchange, 200, json.toString());
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        // Read JSON body
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        Map<String, String> data = JsonUtil.parseJson(body.toString());
        if (data.isEmpty() || !data.containsKey("productName") || !data.containsKey("supplierId")) {
            sendResponse(exchange, 400, "400 Bad Request: Missing required product fields");
            return;
        }

        String supplierId  = data.get("supplierId");
        String productName = data.get("productName");
        String category    = data.getOrDefault("category", "Vegetables");
        
        int unitPrice = 0;
        try { unitPrice = Integer.parseInt(data.getOrDefault("unitPrice", "0")); } catch (Exception e) {}
        
        int stockCount = 0;
        try { stockCount = Integer.parseInt(data.getOrDefault("stockCount", "0")); } catch (Exception e) {}

        Product p = new Product(null, supplierId, productName, category, unitPrice, stockCount);
        storage.save(p);

        // Return newly created product as JSON
        String responseJson = String.format(
            "{\"productId\":\"%s\",\"supplierId\":\"%s\",\"productName\":\"%s\",\"category\":\"%s\",\"unitPrice\":%d,\"stockCount\":%d}",
            escape(p.getProductId()), escape(p.getSupplierId()), escape(p.getProductName()), escape(p.getCategory()), p.getUnitPrice(), p.getStockCount()
        );

        sendJsonResponse(exchange, 201, responseJson);
    }

    private void sendJsonResponse(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        CORSHandler.addCorsHeaders(exchange);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendResponse(HttpExchange exchange, int status, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        CORSHandler.addCorsHeaders(exchange);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}

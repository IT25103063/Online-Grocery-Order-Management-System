import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SupplierHandler implements HttpHandler {

    private final SupplierFileStorage storage;

    public SupplierHandler(SupplierFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Always handle CORS preflight first
        if (CORSHandler.handlePreflight(exchange)) return;

        String method = exchange.getRequestMethod().toUpperCase();
        String path   = exchange.getRequestURI().getPath();   // e.g. /api/suppliers or /api/suppliers/stats
        String query  = exchange.getRequestURI().getQuery();  // e.g. id=SUP-001&status=active

        try {
            // Route: GET /api/suppliers/stats
            if ("GET".equals(method) && path.endsWith("/stats")) {
                handleStats(exchange);
                return;
            }

            switch (method) {
                case "GET":    handleGet(exchange, query);    break;
                case "POST":   handlePost(exchange);          break;
                case "PUT":    handlePut(exchange, query);    break;
                case "DELETE": handleDelete(exchange, query); break;
                default:
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\":\"Internal server error: " + escapeMsg(e.getMessage()) + "\"}");
        }
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        Map<String, String> params = parseQuery(query);

        // GET /api/suppliers?id=SUP-001  — single supplier
        if (params.containsKey("id")) {
            String id = params.get("id");
            Supplier s = storage.findById(id);
            if (s == null) {
                sendResponse(exchange, 404, "{\"error\":\"Supplier not found: " + escapeMsg(id) + "\"}");
            } else {
                sendResponse(exchange, 200, JsonUtil.toJson(s));
            }
            return;
        }

        List<Supplier> all = storage.readAll(); // Changed readAll from storage

        // GET /api/suppliers?status=active  — filter by status
        if (params.containsKey("status")) {
            String status = params.get("status").toLowerCase();
            all = all.stream()
                    .filter(s -> status.equals(s.getStatus()))
                    .collect(Collectors.toList());
            sendResponse(exchange, 200, JsonUtil.toJsonArray(all));
            return;
        }

        // GET /api/suppliers?search=valley  — search by name or category
        if (params.containsKey("search")) {
            String q = params.get("search").toLowerCase();
            all = all.stream()
                    .filter(s -> s.getSupplierName().toLowerCase().contains(q)
                              || (s.getCategories() != null && s.getCategories().stream()
                                    .anyMatch(c -> c.toLowerCase().contains(q))))
                    .collect(Collectors.toList());
            sendResponse(exchange, 200, JsonUtil.toJsonArray(all));
            return;
        }

        // GET /api/suppliers  — return all
        sendResponse(exchange, 200, JsonUtil.toJsonArray(all));
    }

    // ── GET /stats ───────────────────────────────────────────────────────────

    private void handleStats(HttpExchange exchange) throws IOException {
        List<Supplier> all = storage.readAll();
        long total    = all.size();
        long active   = all.stream().filter(s -> "active".equalsIgnoreCase(s.getStatus())).count();
        long inactive = total - active;
        double avgRating = all.isEmpty() ? 0.0
                : all.stream().mapToInt(Supplier::getRating).average().orElse(0.0);
        // Format avgRating to 1 decimal place
        String avg = String.format(Locale.US, "%.1f", avgRating);

        String json = "{"
                + "\"total\":"        + total    + ","
                + "\"active\":"       + active   + ","
                + "\"inactive\":"     + inactive + ","
                + "\"avgRating\":"    + avg      + ","
                + "\"totalProducts\":" + (total * 4)
                + "}";
        sendResponse(exchange, 200, json);
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Map<String, String> data = JsonUtil.parseJson(body);

        String supplierName = data.getOrDefault("supplierName", "").trim();
        if (supplierName.isEmpty()) {
            sendResponse(exchange, 400, "{\"error\":\"supplierName is required\"}");
            return;
        }

        // Auto-generate ID: find max existing numeric suffix
        List<Supplier> all = storage.readAll();
        int maxNum = 0;
        for (Supplier s : all) {
            String sid = s.getId(); // SUP-001
            if (sid != null && sid.startsWith("SUP-")) {
                try {
                    int num = Integer.parseInt(sid.substring(4));
                    if (num > maxNum) maxNum = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        String newId = String.format("SUP-%03d", maxNum + 1);

        String today = LocalDate.now().toString();

        // Parse categories — stored as comma-separated in the map
        List<String> categories = new ArrayList<>();
        String catsRaw = data.getOrDefault("categories", "").trim();
        if (!catsRaw.isEmpty()) {
            for (String c : catsRaw.split(",")) {
                String ct = c.trim();
                if (!ct.isEmpty()) categories.add(ct);
            }
        }

        Supplier s = new Supplier();
        s.setId(newId);
        s.setSupplierName(supplierName);
        s.setContactPerson(data.getOrDefault("contactPerson", "").trim());
        s.setEmail(data.getOrDefault("email", "").trim());
        s.setPhone(data.getOrDefault("phone", "").trim());
        s.setAddress(data.getOrDefault("address", "").trim());
        s.setCategories(categories);
        s.setPaymentTerms(data.getOrDefault("paymentTerms", "").trim());
        s.setLeadTime(parseIntSafe(data.getOrDefault("leadTime", "0")));
        s.setRating(parseIntSafe(data.getOrDefault("rating", "0")));
        s.setStatus(data.getOrDefault("status", "active").trim().toLowerCase());
        s.setCreatedDate(today);
        s.setLastUpdated(today);

        storage.save(s);
        sendResponse(exchange, 201, JsonUtil.toJson(s));
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    private void handlePut(HttpExchange exchange, String query) throws IOException {
        Map<String, String> params = parseQuery(query);
        String id = params.get("id");
        if (id == null || id.trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\":\"Query param 'id' is required for PUT\"}");
            return;
        }

        Supplier existing = storage.findById(id);
        if (existing == null) {
            sendResponse(exchange, 404, "{\"error\":\"Supplier not found: " + escapeMsg(id) + "\"}");
            return;
        }

        String body = readBody(exchange);
        Map<String, String> data = JsonUtil.parseJson(body);

        // Update only fields that are provided in the request body
        if (data.containsKey("supplierName"))  existing.setSupplierName(data.get("supplierName").trim());
        if (data.containsKey("contactPerson")) existing.setContactPerson(data.get("contactPerson").trim());
        if (data.containsKey("email"))         existing.setEmail(data.get("email").trim());
        if (data.containsKey("phone"))         existing.setPhone(data.get("phone").trim());
        if (data.containsKey("address"))       existing.setAddress(data.get("address").trim());
        if (data.containsKey("paymentTerms"))  existing.setPaymentTerms(data.get("paymentTerms").trim());
        if (data.containsKey("leadTime"))      existing.setLeadTime(parseIntSafe(data.get("leadTime")));
        if (data.containsKey("rating"))        existing.setRating(parseIntSafe(data.get("rating")));
        if (data.containsKey("status"))        existing.setStatus(data.get("status").trim().toLowerCase());

        if (data.containsKey("categories")) {
            List<String> categories = new ArrayList<>();
            String catsRaw = data.get("categories").trim();
            if (!catsRaw.isEmpty()) {
                for (String c : catsRaw.split(",")) {
                    String ct = c.trim();
                    if (!ct.isEmpty()) categories.add(ct);
                }
            }
            existing.setCategories(categories);
        }

        existing.setLastUpdated(LocalDate.now().toString());
        storage.update(existing);
        sendResponse(exchange, 200, JsonUtil.toJson(existing));
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        Map<String, String> params = parseQuery(query);
        String id = params.get("id");
        if (id == null || id.trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\":\"Query param 'id' is required for DELETE\"}");
            return;
        }

        Supplier existing = storage.findById(id);
        if (existing == null) {
            sendResponse(exchange, 404, "{\"error\":\"Supplier not found: " + escapeMsg(id) + "\"}");
            return;
        }

        storage.delete(id);
        sendResponse(exchange, 200, "{\"message\":\"Supplier " + escapeMsg(id) + " deleted successfully\"}");
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    /** Read full request body as UTF-8 string. */
    private String readBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(chunk)) != -1) {
            buffer.write(chunk, 0, bytesRead);
        }
        return buffer.toString(StandardCharsets.UTF_8.name());
    }

    /** Parse a URL query string into a key/value map. */
    private Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.trim().isEmpty()) return map;
        for (String part : query.split("&")) {
            int eq = part.indexOf('=');
            if (eq > 0) {
                String key = part.substring(0, eq).trim();
                String val = part.substring(eq + 1).trim();
                try {
                    val = java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name());
                } catch (Exception ignored) {}
                map.put(key, val);
            }
        }
        return map;
    }

    /** Send a JSON response with CORS headers. */
    private void sendResponse(HttpExchange exchange, int status, String jsonBody) throws IOException {
        CORSHandler.addCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private int parseIntSafe(String s) {
        if (s == null) return 0;
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }

    private String escapeMsg(String s) {
        if (s == null) return "";
        return s.replace("\"", "'");
    }
}

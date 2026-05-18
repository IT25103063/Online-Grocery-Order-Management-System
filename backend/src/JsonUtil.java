import java.util.*;

public class JsonUtil {

    // Escape special characters in a JSON string value
    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Serialize a single Supplier to a JSON string
    public static String toJson(Supplier s) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(escapeJson(s.getId())).append("\",");
        sb.append("\"supplierName\":\"").append(escapeJson(s.getSupplierName())).append("\",");
        sb.append("\"contactPerson\":\"").append(escapeJson(s.getContactPerson())).append("\",");
        sb.append("\"email\":\"").append(escapeJson(s.getEmail())).append("\",");
        sb.append("\"phone\":\"").append(escapeJson(s.getPhone())).append("\",");
        sb.append("\"address\":\"").append(escapeJson(s.getAddress())).append("\",");
        sb.append("\"categories\":[");
        List<String> cats = s.getCategories();
        if (cats != null) {
            for (int i = 0; i < cats.size(); i++) {
                sb.append("\"").append(escapeJson(cats.get(i))).append("\"");
                if (i < cats.size() - 1) sb.append(",");
            }
        }
        sb.append("],");
        sb.append("\"paymentTerms\":\"").append(escapeJson(s.getPaymentTerms())).append("\",");
        sb.append("\"leadTime\":").append(s.getLeadTime()).append(",");
        sb.append("\"rating\":").append(s.getRating()).append(",");
        sb.append("\"status\":\"").append(escapeJson(s.getStatus())).append("\",");
        sb.append("\"createdDate\":\"").append(escapeJson(s.getCreatedDate())).append("\",");
        sb.append("\"lastUpdated\":\"").append(escapeJson(s.getLastUpdated())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    // Serialize a list of Suppliers to a JSON array string
    public static String toJsonArray(List<Supplier> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Simple flat JSON key/value parser for request bodies.
     * Handles: strings, numbers, booleans, and arrays of strings.
     * Returns all values as strings. Arrays are joined with commas.
     * Does NOT handle nested objects.
     *
     * Example input:
     *   {"supplierName":"Fresh Farms","leadTime":3,"categories":["Vegetables","Fruits"]}
     * Resulting map:
     *   supplierName -> Fresh Farms
     *   leadTime     -> 3
     *   categories   -> Vegetables,Fruits
     */
    public static Map<String, String> parseJson(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        if (json == null || json.trim().isEmpty()) return result;

        json = json.trim();
        // Strip outer braces
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);
        json = json.trim();

        // We'll parse character by character to handle nested arrays correctly
        int i = 0;
        while (i < json.length()) {
            // Skip whitespace and commas between entries
            while (i < json.length() && (json.charAt(i) == ',' || Character.isWhitespace(json.charAt(i)))) i++;
            if (i >= json.length()) break;

            // Expect a quoted key
            if (json.charAt(i) != '"') { i++; continue; }
            i++; // skip opening quote
            StringBuilder keyBuf = new StringBuilder();
            while (i < json.length() && json.charAt(i) != '"') {
                if (json.charAt(i) == '\\') i++; // skip escape char
                if (i < json.length()) keyBuf.append(json.charAt(i));
                i++;
            }
            i++; // skip closing quote of key

            // Skip whitespace and colon
            while (i < json.length() && (json.charAt(i) == ':' || Character.isWhitespace(json.charAt(i)))) i++;
            if (i >= json.length()) break;

            char c = json.charAt(i);
            String value;

            if (c == '"') {
                // String value
                i++; // skip opening quote
                StringBuilder valBuf = new StringBuilder();
                while (i < json.length() && json.charAt(i) != '"') {
                    if (json.charAt(i) == '\\') {
                        i++; // skip backslash
                        if (i < json.length()) {
                            char esc = json.charAt(i);
                            switch (esc) {
                                case 'n': valBuf.append('\n'); break;
                                case 'r': valBuf.append('\r'); break;
                                case 't': valBuf.append('\t'); break;
                                default:  valBuf.append(esc); break;
                            }
                        }
                    } else {
                        valBuf.append(json.charAt(i));
                    }
                    i++;
                }
                i++; // skip closing quote
                value = valBuf.toString();

            } else if (c == '[') {
                // Array value — collect items as comma-separated string
                i++; // skip '['
                List<String> items = new ArrayList<>();
                while (i < json.length() && json.charAt(i) != ']') {
                    while (i < json.length() && (json.charAt(i) == ',' || Character.isWhitespace(json.charAt(i)))) i++;
                    if (i < json.length() && json.charAt(i) == '"') {
                        i++; // skip opening quote
                        StringBuilder itemBuf = new StringBuilder();
                        while (i < json.length() && json.charAt(i) != '"') {
                            if (json.charAt(i) == '\\') i++;
                            if (i < json.length()) itemBuf.append(json.charAt(i));
                            i++;
                        }
                        i++; // skip closing quote
                        items.add(itemBuf.toString());
                    } else if (i < json.length() && json.charAt(i) != ']') {
                        i++; // skip unexpected char
                    }
                }
                i++; // skip ']'
                value = String.join(",", items);

            } else {
                // Number or boolean: read until comma, } or whitespace
                StringBuilder valBuf = new StringBuilder();
                while (i < json.length() && json.charAt(i) != ',' && json.charAt(i) != '}' && !Character.isWhitespace(json.charAt(i))) {
                    valBuf.append(json.charAt(i));
                    i++;
                }
                value = valBuf.toString().trim();
            }

            String key = keyBuf.toString().trim();
            if (!key.isEmpty()) {
                result.put(key, value);
            }
        }

        return result;
    }
}

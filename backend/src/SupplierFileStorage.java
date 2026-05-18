import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SupplierFileStorage.java
 * Data Access Layer — reads and writes supplier records to/from data/suppliers.txt.
 *
 * File format: one supplier per line, pipe-delimited (see Supplier.toFileString()).
 * Lines starting with '#' are treated as comments and ignored.
 * Blank lines are skipped.
 *
 * All public methods are synchronized to prevent concurrent read/write corruption
 * when multiple HTTP requests arrive at the same time.
 */
public class SupplierFileStorage {

    // ── Configuration ─────────────────────────────────────────────────────────

    /** Path to the flat-file data store. Relative to the working directory. */
    private static final String FILE_PATH = "data/suppliers.txt";

    /** ID prefix and zero-pad width. e.g. SUP-001, SUP-012, SUP-100 */
    private static final String ID_PREFIX = "SUP-";
    private static final int    ID_PAD    = 3;

    // ── Singleton / Constructor ───────────────────────────────────────────────

    private final String filePath;

    /** Default constructor — uses data/suppliers.txt in the working directory. */
    public SupplierFileStorage() {
        this(FILE_PATH);
    }

    /** Constructor with a custom file path (useful for testing). */
    public SupplierFileStorage(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    // ── File Bootstrap ────────────────────────────────────────────────────────

    /**
     * Create the data file if it does not already exist.
     * Writes a comment header so the file is human-readable.
     */
    private void ensureFileExists() {
        File f = new File(filePath);
        // Ensure parent directories exist
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        
        if (!f.exists()) {
            try {
                f.createNewFile();
                writeComment("FreshLink Supplier Data File");
                writeComment("Format: id|supplierName|contactPerson|email|phone|address|categories|paymentTerms|leadTime|rating|status|createdDate|lastUpdated");
                writeComment("Do not edit manually while the server is running.");
                System.out.println("[Storage] Created new data file: " + f.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("[Storage] ERROR: Could not create data file: " + e.getMessage());
            }
        }
    }

    /** Append a comment line (starts with #) to the file. */
    private void writeComment(String comment) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write("# " + comment);
            bw.newLine();
        }
    }

    // ── READ Operations ───────────────────────────────────────────────────────

    /**
     * Read all suppliers from the file.
     *
     * @return List of all valid Supplier records (never null, may be empty)
     */
    public synchronized List<Supplier> readAll() {
        List<Supplier> suppliers = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) return suppliers;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Skip blank lines and comment lines
                if (line.isEmpty() || line.startsWith("#")) continue;

                Supplier s = Supplier.fromFileString(line);
                if (s != null) {
                    suppliers.add(s);
                }
            }
        } catch (IOException e) {
            System.err.println("[Storage] ERROR reading file: " + e.getMessage());
        }

        return suppliers;
    }

    /**
     * Find a single supplier by ID (case-insensitive).
     *
     * @param id supplier ID, e.g. "SUP-001"
     * @return the matching Supplier, or null if not found
     */
    public synchronized Supplier findById(String id) {
        if (id == null || id.trim().isEmpty()) return null;

        for (Supplier s : readAll()) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Filter suppliers by status.
     *
     * @param status "active" or "inactive"
     * @return filtered list (never null)
     */
    public synchronized List<Supplier> findByStatus(String status) {
        List<Supplier> result = new ArrayList<>();
        for (Supplier s : readAll()) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Search suppliers by name OR category (case-insensitive, partial match).
     *
     * @param query search term
     * @return matching suppliers (never null)
     */
    public synchronized List<Supplier> search(String query) {
        List<Supplier> result = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return readAll();

        String q = query.trim().toLowerCase();

        for (Supplier s : readAll()) {
            boolean nameMatch = s.getSupplierName().toLowerCase().contains(q);
            boolean catMatch  = s.getCategories().stream()
                                  .anyMatch(c -> c.toLowerCase().contains(q));
            boolean contactMatch = s.getContactPerson().toLowerCase().contains(q);

            if (nameMatch || catMatch || contactMatch) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Compute summary statistics across all suppliers.
     *
     * @return SupplierStats object
     */
    public synchronized SupplierStats getStats() {
        List<Supplier> all = readAll();
        int total    = all.size();
        int active   = 0;
        int inactive = 0;
        int ratingSum = 0;

        for (Supplier s : all) {
            if ("active".equalsIgnoreCase(s.getStatus())) active++;
            else inactive++;
            ratingSum += s.getRating();
        }

        double avgRating = (total > 0) ? Math.round((double) ratingSum / total * 10.0) / 10.0 : 0.0;

        // totalProducts: mock value — replace with real product count when products are implemented
        int totalProducts = total * 4;

        return new SupplierStats(total, active, inactive, avgRating, totalProducts);
    }

    // ── WRITE Operations ──────────────────────────────────────────────────────

    /**
     * Append a new supplier record to the file.
     * Auto-generates the ID and sets createdDate / lastUpdated to today.
     *
     * @param s Supplier to persist (id will be overwritten with generated value)
     * @throws IOException if the file cannot be written
     */
    public synchronized void save(Supplier s) throws IOException {
        s.setId(generateNextId());
        s.setCreatedDate(LocalDate.now().toString());
        s.setLastUpdated(LocalDate.now().toString());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(s.toFileString());
            bw.newLine();
        }

        System.out.println("[Storage] Saved new supplier: " + s.getId() + " — " + s.getSupplierName());
    }

    /**
     * Update an existing supplier record.
     * Rewrites the entire file, replacing the line whose ID matches.
     *
     * @param updated Supplier with updated fields (ID must match an existing record)
     * @throws IOException              if the file cannot be read or written
     * @throws IllegalArgumentException if no supplier with that ID exists
     */
    public synchronized void update(Supplier updated) throws IOException {
        updated.setLastUpdated(LocalDate.now().toString());

        List<Supplier> all      = readAll();
        boolean        found    = false;
        StringBuilder  sb       = new StringBuilder();

        for (Supplier s : all) {
            if (s.getId().equalsIgnoreCase(updated.getId())) {
                // Preserve original createdDate
                updated.setCreatedDate(s.getCreatedDate());
                sb.append(updated.toFileString()).append(System.lineSeparator());
                found = true;
            } else {
                sb.append(s.toFileString()).append(System.lineSeparator());
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Supplier not found: " + updated.getId());
        }

        writeToFile(sb.toString());
        System.out.println("[Storage] Updated supplier: " + updated.getId());
    }

    /**
     * Delete a supplier by ID.
     * Rewrites the entire file, omitting the matching record.
     *
     * @param id supplier ID to delete
     * @throws IOException              if the file cannot be read or written
     * @throws IllegalArgumentException if no supplier with that ID exists
     */
    public synchronized void delete(String id) throws IOException {
        List<Supplier> all   = readAll();
        boolean        found = false;
        StringBuilder  sb    = new StringBuilder();

        for (Supplier s : all) {
            if (s.getId().equalsIgnoreCase(id)) {
                found = true; // skip this record (effectively deletes it)
            } else {
                sb.append(s.toFileString()).append(System.lineSeparator());
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Supplier not found: " + id);
        }

        writeToFile(sb.toString());
        System.out.println("[Storage] Deleted supplier: " + id);
    }

    // ── ID Generation ─────────────────────────────────────────────────────────

    /**
     * Generate the next sequential supplier ID by scanning existing IDs.
     * Format: SUP-001, SUP-002, ... SUP-999
     *
     * @return next available ID string
     */
    public synchronized String generateNextId() {
        int max = 0;

        for (Supplier s : readAll()) {
            String id = s.getId();
            if (id != null && id.startsWith(ID_PREFIX)) {
                try {
                    int num = Integer.parseInt(id.substring(ID_PREFIX.length()));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {
                    // Non-numeric suffix — ignore
                }
            }
        }

        int    next   = max + 1;
        String padded = String.format("%0" + ID_PAD + "d", next);
        return ID_PREFIX + padded;
    }

    // ── Seed Data ─────────────────────────────────────────────────────────────

    /**
     * Seed the file with sample suppliers if it is currently empty.
     * Called from Main.java on startup.
     */
    public synchronized void seedIfEmpty() {
        if (!readAll().isEmpty()) return; // already has data

        System.out.println("[Storage] Seeding sample supplier data...");

        Object[][] seed = {
            // name, contact, email, phone, address, categories, payment, lead, rating, status
            {"Green Valley Farms",   "Amara Nwosu",    "amara@greenvalley.lk",   "+94771234567", "123 Farm Lane, Kandy, Central Province",          "Vegetables",        "Net 30", 3, 5, "active"},
            {"Sunrise Dairy Co.",    "Priya Mehta",    "priya@sunrisedairy.lk",  "+94719876543", "45 Dairy Road, Nuwara Eliya, Central Province",   "Dairy",             "Net 15", 2, 4, "active"},
            {"Tropical Fresh Ltd.",  "Carlos Reyes",   "carlos@tropicalfresh.lk","+94765550123", "78 Orchard Drive, Galle, Southern Province",      "Fruits",            "Net 45", 5, 4, "active"},
            {"Heritage Bakery",      "Emma Thompson",  "emma@heritagebakery.lk", "+94770001122", "12 Baker Street, Colombo 03, Western Province",   "Bakery",            "COD",    1, 3, "inactive"},
            {"Prime Meats & More",   "James Owens",    "james@primemeats.lk",    "+94762233445", "56 Butchers Lane, Kurunegala, North Western",     "Meat",              "Net 30", 4, 5, "active"},
        };

        String today = LocalDate.now().toString();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            int num = 1;
            for (Object[] row : seed) {
                String id = ID_PREFIX + String.format("%0" + ID_PAD + "d", num++);

                List<String> cats = new ArrayList<>();
                for (String c : ((String) row[5]).split(",")) {
                    cats.add(c.trim());
                }

                Supplier s = new Supplier(
                    id,
                    (String)  row[0],
                    (String)  row[1],
                    (String)  row[2],
                    (String)  row[3],
                    (String)  row[4],
                    cats,
                    (String)  row[6],
                    (Integer) row[7],
                    (Integer) row[8],
                    (String)  row[9],
                    today,
                    today
                );

                bw.write(s.toFileString());
                bw.newLine();
                System.out.println("[Storage]  + Seeded: " + id + " — " + s.getSupplierName());
            }
        } catch (IOException e) {
            System.err.println("[Storage] ERROR seeding data: " + e.getMessage());
        }
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    /**
     * Atomically overwrite the entire file with new content.
     * Uses a temp file + rename for safety.
     *
     * @param content full file content to write
     * @throws IOException if write or rename fails
     */
    private void writeToFile(String content) throws IOException {
        Path target = Paths.get(filePath);
        Path temp   = Paths.get(filePath + ".tmp");

        Files.writeString(temp, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    // ── Inner Stats Class ─────────────────────────────────────────────────────

    /**
     * Simple value object returned by getStats().
     * Turned into JSON by JsonUtil.
     */
    public static class SupplierStats {
        public final int    total;
        public final int    active;
        public final int    inactive;
        public final double avgRating;
        public final int    totalProducts;

        public SupplierStats(int total, int active, int inactive,
                             double avgRating, int totalProducts) {
            this.total         = total;
            this.active        = active;
            this.inactive      = inactive;
            this.avgRating     = avgRating;
            this.totalProducts = totalProducts;
        }
    }
}

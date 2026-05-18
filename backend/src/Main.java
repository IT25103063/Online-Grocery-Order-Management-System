import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class Main {

    private static final int PORT = 8080;
    private static final String DATA_FILE = "data/suppliers.txt";
    private static final String PRODUCTS_FILE = "data/products.txt";

    public static void main(String[] args) throws Exception {

        // ── Storage ──────────────────────────────────────────────────────────
        SupplierFileStorage storage = new SupplierFileStorage(DATA_FILE);
        ProductFileStorage productStorage = new ProductFileStorage(PRODUCTS_FILE);

        // Seed only if the file is empty or doesn't exist
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists() || dataFile.length() == 0) {
            System.out.println("No data file found — seeding 5 sample suppliers...");
            seedSampleData(storage);
        }

        // ── Auto Backup ──────────────────────────────────────────────────────
        System.out.println("Performing automatic database backups...");
        backupDatabase();

        // ── Scan & Dynamic Port Resolution ──────────────────────────────────
        int actualPort = findFreePort(PORT);

        // ── HTTP Server ──────────────────────────────────────────────────────
        HttpServer server = HttpServer.create(new InetSocketAddress(actualPort), 0);

        SupplierHandler handler = new SupplierHandler(storage);

        // Route API endpoints
        server.createContext("/api/suppliers", handler);
        server.createContext("/api/products", new ProductHandler(productStorage));

        // Serve static web pages from the 'frontend' folder under root "/"
        server.createContext("/", new StaticFileHandler("frontend"));

        // Use a fixed thread pool so concurrent requests don't block each other
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();

        System.out.println("\n=================================================================");
        System.out.println("FreshLink Web Application Server successfully booted!");
        System.out.println("Server running at: http://localhost:" + actualPort);
        System.out.println("=================================================================");
        System.out.println("Press Ctrl+C to stop.");

        // ── Launch Browser ───────────────────────────────────────────────────
        launchBrowser(actualPort);

        // ── Graceful shutdown on Ctrl+C ───────────────────────────────────────
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server...");
            server.stop(1);
            System.out.println("Server stopped.");
        }));
    }

    private static int findFreePort(int startPort) {
        int port = startPort;
        while (port < startPort + 100) {
            try (java.net.ServerSocket socket = new java.net.ServerSocket(port)) {
                socket.getReuseAddress(); // use variable to prevent unused resource warning
                return port; // successfully opened, port is free
            } catch (Exception e) {
                port++; // try next port
            }
        }
        return startPort;
    }

    private static void backupDatabase() {
        File backupDir = new File("data/backups");
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        String timestamp = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        backupFile(new File(DATA_FILE), new File(backupDir, "suppliers_" + timestamp + ".txt"));
        backupFile(new File(PRODUCTS_FILE), new File(backupDir, "products_" + timestamp + ".txt"));
    }

    private static void backupFile(File source, File dest) {
        if (!source.exists() || source.length() == 0) return;
        try {
            java.nio.file.Files.copy(source.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("  + Backup created successfully: " + dest.getName());
        } catch (Exception e) {
            System.out.println("  - Backup failed for " + source.getName() + ": " + e.getMessage());
        }
    }

    private static void launchBrowser(int port) {
        String url = "http://localhost:" + port + "/index.html";
        System.out.println("Launching default web browser to: " + url);
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else {
                new ProcessBuilder("xdg-open", url).start();
            }
        } catch (Exception e) {
            System.out.println("  - Auto-launch browser failed: " + e.getMessage());
            System.out.println("  - Please open your web browser manually and navigate to: " + url);
        }
    }

    // ── Sample data ──────────────────────────────────────────────────────────

    private static void seedSampleData(SupplierFileStorage storage) {
        String today = LocalDate.now().toString();

        Supplier s1 = new Supplier();
        s1.setId("SUP-001");
        s1.setSupplierName("Fresh Farms");
        s1.setContactPerson("Amara Nwosu");
        s1.setEmail("amara@freshfarms.com");
        s1.setPhone("+94 77 123 4567");
        s1.setAddress("12 Greenway Road, Colombo 07, Sri Lanka");
        s1.setCategories(Arrays.asList("Vegetables", "Fruits"));
        s1.setPaymentTerms("Net 30");
        s1.setLeadTime(2);
        s1.setRating(5);
        s1.setStatus("active");
        s1.setCreatedDate(today);
        s1.setLastUpdated(today);
        try { storage.save(s1); } catch (Exception e) { e.printStackTrace(); }

        Supplier s2 = new Supplier();
        s2.setId("SUP-002");
        s2.setSupplierName("Daily Dairy");
        s2.setContactPerson("Priya Mehta");
        s2.setEmail("priya@dailydairy.com");
        s2.setPhone("+94 71 234 5678");
        s2.setAddress("45 Milk Lane, Kandy, Sri Lanka");
        s2.setCategories(Arrays.asList("Dairy"));
        s2.setPaymentTerms("Net 15");
        s2.setLeadTime(1);
        s2.setRating(4);
        s2.setStatus("active");
        s2.setCreatedDate(today);
        s2.setLastUpdated(today);
        try { storage.save(s2); } catch (Exception e) { e.printStackTrace(); }

        Supplier s3 = new Supplier();
        s3.setId("SUP-003");
        s3.setSupplierName("Organic Valley");
        s3.setContactPerson("Carlos Reyes");
        s3.setEmail("carlos@organicvalley.com");
        s3.setPhone("+94 76 345 6789");
        s3.setAddress("88 Harvest Hill, Nuwara Eliya, Sri Lanka");
        s3.setCategories(Arrays.asList("Vegetables", "Fruits", "Beverages"));
        s3.setPaymentTerms("Net 45");
        s3.setLeadTime(3);
        s3.setRating(5);
        s3.setStatus("active");
        s3.setCreatedDate(today);
        s3.setLastUpdated(today);
        try { storage.save(s3); } catch (Exception e) { e.printStackTrace(); }

        Supplier s4 = new Supplier();
        s4.setId("SUP-004");
        s4.setSupplierName("Meat Masters");
        s4.setContactPerson("James Owens");
        s4.setEmail("james@meatmasters.com");
        s4.setPhone("+94 72 456 7890");
        s4.setAddress("22 Butcher Street, Galle, Sri Lanka");
        s4.setCategories(Arrays.asList("Meat"));
        s4.setPaymentTerms("COD");
        s4.setLeadTime(1);
        s4.setRating(4);
        s4.setStatus("active");
        s4.setCreatedDate(today);
        s4.setLastUpdated(today);
        try { storage.save(s4); } catch (Exception e) { e.printStackTrace(); }

        Supplier s5 = new Supplier();
        s5.setId("SUP-005");
        s5.setSupplierName("Bakery Bliss");
        s5.setContactPerson("Emma Thompson");
        s5.setEmail("emma@bakerybliss.com");
        s5.setPhone("+94 70 567 8901");
        s5.setAddress("7 Flour Court, Matara, Sri Lanka");
        s5.setCategories(Arrays.asList("Bakery"));
        s5.setPaymentTerms("Net 30");
        s5.setLeadTime(2);
        s5.setRating(3);
        s5.setStatus("inactive");
        s5.setCreatedDate(today);
        s5.setLastUpdated(today);
        try { storage.save(s5); } catch (Exception e) { e.printStackTrace(); }

        System.out.println("Seeded 5 sample suppliers into " + DATA_FILE);
    }
}

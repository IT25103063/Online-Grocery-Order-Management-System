import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductFileStorage
 * Thread-safe flat-file database manager for products.txt.
 */
public class ProductFileStorage {

    private final String filePath;

    public ProductFileStorage(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all products from the flat file.
     */
    public synchronized List<Product> getAll() throws IOException {
        List<Product> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product p = Product.fromFileString(line);
                if (p != null) {
                    list.add(p);
                }
            }
        }
        return list;
    }

    /**
     * Reads products filtered by supplierId.
     */
    public synchronized List<Product> getBySupplier(String supplierId) throws IOException {
        List<Product> matching = new ArrayList<>();
        if (supplierId == null || supplierId.isEmpty()) return matching;

        for (Product p : getAll()) {
            if (supplierId.equalsIgnoreCase(p.getSupplierId())) {
                matching.add(p);
            }
        }
        return matching;
    }

    /**
     * Saves (appends) a new product to the file.
     */
    public synchronized void save(Product p) throws IOException {
        if (p == null) return;
        
        // Auto-assign product ID if not present
        if (p.getProductId() == null || p.getProductId().isEmpty()) {
            p.setProductId("PROD-" + (getAll().size() + 1));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(p.toFileString());
            writer.newLine();
        }
    }
}

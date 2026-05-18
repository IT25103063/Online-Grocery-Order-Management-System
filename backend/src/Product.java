/**
 * Product.java
 * Model / POJO for a Product entity.
 *
 * File storage format:
 *   productId|supplierId|productName|category|unitPrice|stockCount
 */
public class Product {

    private String productId;
    private String supplierId;
    private String productName;
    private String category;
    private int    unitPrice;
    private int    stockCount;

    public Product() {}

    public Product(String productId, String supplierId, String productName, String category, int unitPrice, int stockCount) {
        this.productId   = productId;
        this.supplierId  = supplierId;
        this.productName = productName;
        this.category    = category;
        this.unitPrice   = unitPrice;
        this.stockCount  = stockCount;
    }

    public String toFileString() {
        return String.join("|",
            safe(productId),
            safe(supplierId),
            safe(productName),
            safe(category),
            String.valueOf(unitPrice),
            String.valueOf(stockCount)
        );
    }

    public static Product fromFileString(String line) {
        if (line == null || line.trim().isEmpty() || line.trim().startsWith("#")) {
            return null;
        }

        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            return null;
        }

        try {
            String productId   = parts[0].trim();
            String supplierId  = parts[1].trim();
            String productName = parts[2].trim();
            String category    = parts[3].trim();
            int    unitPrice   = Integer.parseInt(parts[4].trim());
            int    stockCount  = Integer.parseInt(parts[5].trim());

            return new Product(productId, supplierId, productName, category, unitPrice, stockCount);
        } catch (Exception e) {
            return null;
        }
    }

    private static String safe(String value) {
        if (value == null) return "";
        return value.replace("|", "");
    }

    // Getters and Setters
    public String getProductId()                  { return productId; }
    public void   setProductId(String v)          { this.productId = v; }

    public String getSupplierId()                 { return supplierId; }
    public void   setSupplierId(String v)         { this.supplierId = v; }

    public String getProductName()                { return productName; }
    public void   setProductName(String v)        { this.productName = v; }

    public String getCategory()                   { return category; }
    public void   setCategory(String v)           { this.category = v; }

    public int    getUnitPrice()                  { return unitPrice; }
    public void   setUnitPrice(int v)             { this.unitPrice = v; }

    public int    getStockCount()                 { return stockCount; }
    public void   setStockCount(int v)            { this.stockCount = v; }
}

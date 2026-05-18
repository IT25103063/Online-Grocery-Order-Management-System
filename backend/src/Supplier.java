import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier.java
 * Model / POJO for a Supplier entity.
 *
 * File storage format (pipe-delimited, one supplier per line):
 *   id|supplierName|contactPerson|email|phone|address|categories|paymentTerms|leadTime|rating|status|createdDate|lastUpdated
 *
 * 'categories' is stored as a comma-joined string inside the pipe-delimited line.
 * Example line:
 *   SUP-001|Green Valley Farms|Amara Nwosu|amara@greenvalley.lk|+94771234567|123 Farm Lane Kandy|Vegetables,Fruits|Net 30|3|5|active|2025-03-12|2026-01-15
 */
public class Supplier {

    // ── Fields ────────────────────────────────────────────────────────────────

    private String id;
    private String supplierName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private List<String> categories;   // e.g. ["Vegetables", "Fruits"]
    private String paymentTerms;       // Net 15 | Net 30 | Net 45 | COD
    private int    leadTime;           // days
    private int    rating;             // 1 – 5
    private String status;             // active | inactive
    private String createdDate;        // yyyy-MM-dd
    private String lastUpdated;        // yyyy-MM-dd

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Default constructor (required for deserialization). */
    public Supplier() {
        this.categories  = new ArrayList<>();
        this.createdDate = LocalDate.now().toString();
        this.lastUpdated = LocalDate.now().toString();
    }

    /** Full constructor. */
    public Supplier(String id,
                    String supplierName,
                    String contactPerson,
                    String email,
                    String phone,
                    String address,
                    List<String> categories,
                    String paymentTerms,
                    int    leadTime,
                    int    rating,
                    String status,
                    String createdDate,
                    String lastUpdated) {
        this.id            = id;
        this.supplierName  = supplierName;
        this.contactPerson = contactPerson;
        this.email         = email;
        this.phone         = phone;
        this.address       = address;
        this.categories    = (categories != null) ? categories : new ArrayList<>();
        this.paymentTerms  = paymentTerms;
        this.leadTime      = leadTime;
        this.rating        = rating;
        this.status        = status;
        this.createdDate   = createdDate;
        this.lastUpdated   = lastUpdated;
    }

    // ── Serialization ─────────────────────────────────────────────────────────

    /**
     * Serialize this Supplier to a single pipe-delimited line for text-file storage.
     * Any pipe characters inside field values are stripped to avoid parsing issues.
     *
     * @return pipe-delimited String, no trailing newline
     */
    public String toFileString() {
        return String.join("|",
            safe(id),
            safe(supplierName),
            safe(contactPerson),
            safe(email),
            safe(phone),
            safe(address),                              // address may contain commas – that is fine
            String.join(",", categories),               // categories joined by comma
            safe(paymentTerms),
            String.valueOf(leadTime),
            String.valueOf(rating),
            safe(status),
            safe(createdDate),
            safe(lastUpdated)
        );
    }

    /**
     * Deserialize a Supplier from a pipe-delimited line read from suppliers.txt.
     *
     * @param line one raw line from the file
     * @return Supplier instance, or null if the line is blank / malformed
     */
    public static Supplier fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        // Limit to 13 tokens; extra pipes inside address are already stripped at write time
        String[] parts = line.split("\\|", 13);

        if (parts.length < 13) {
            System.err.println("[WARN] Skipping malformed supplier line (expected 13 fields): " + line);
            return null;
        }

        try {
            String        id            = parts[0].trim();
            String        supplierName  = parts[1].trim();
            String        contactPerson = parts[2].trim();
            String        email         = parts[3].trim();
            String        phone         = parts[4].trim();
            String        address       = parts[5].trim();
            List<String>  categories    = parseCategories(parts[6].trim());
            String        paymentTerms  = parts[7].trim();
            int           leadTime      = parseInt(parts[8].trim(), 0);
            int           rating        = parseInt(parts[9].trim(), 3);
            String        status        = parts[10].trim();
            String        createdDate   = parts[11].trim();
            String        lastUpdated   = parts[12].trim();

            return new Supplier(id, supplierName, contactPerson, email, phone,
                                address, categories, paymentTerms,
                                leadTime, rating, status, createdDate, lastUpdated);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to parse supplier line: " + line);
            e.printStackTrace();
            return null;
        }
    }

    // ── Validation ────────────────────────────────────────────────────────────

    /**
     * Basic validation. Returns an error message, or null if the supplier is valid.
     *
     * @return null if valid, otherwise a human-readable error message
     */
    public String validate() {
        if (supplierName  == null || supplierName.trim().isEmpty())  return "supplierName is required";
        if (contactPerson == null || contactPerson.trim().isEmpty()) return "contactPerson is required";
        if (email         == null || !email.contains("@"))           return "A valid email is required";
        if (phone         == null || phone.trim().isEmpty())         return "phone is required";
        if (address       == null || address.trim().isEmpty())       return "address is required";
        if (categories    == null || categories.isEmpty())           return "At least one category is required";
        if (paymentTerms  == null || paymentTerms.trim().isEmpty())  return "paymentTerms is required";
        if (leadTime < 1)                                            return "leadTime must be >= 1 day";
        if (rating < 1 || rating > 5)                               return "rating must be between 1 and 5";
        if (!"active".equals(status) && !"inactive".equals(status)) return "status must be 'active' or 'inactive'";
        return null; // valid
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Strip pipe characters from a field value to keep the file format safe. */
    private static String safe(String value) {
        if (value == null) return "";
        return value.replace("|", "");
    }

    /** Parse a comma-separated category string into a List. */
    private static List<String> parseCategories(String raw) {
        List<String> list = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return list;
        for (String cat : raw.split(",")) {
            String trimmed = cat.trim();
            if (!trimmed.isEmpty()) list.add(trimmed);
        }
        return list;
    }

    /** Safe integer parsing with a default fallback. */
    private static int parseInt(String raw, int defaultValue) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }

    public String getSupplierName()                { return supplierName; }
    public void   setSupplierName(String v)        { this.supplierName = v; }

    public String getContactPerson()               { return contactPerson; }
    public void   setContactPerson(String v)       { this.contactPerson = v; }

    public String getEmail()                       { return email; }
    public void   setEmail(String v)               { this.email = v; }

    public String getPhone()                       { return phone; }
    public void   setPhone(String v)               { this.phone = v; }

    public String getAddress()                     { return address; }
    public void   setAddress(String v)             { this.address = v; }

    public List<String> getCategories()            { return categories; }
    public void   setCategories(List<String> v)    { this.categories = v; }

    public String getPaymentTerms()                { return paymentTerms; }
    public void   setPaymentTerms(String v)        { this.paymentTerms = v; }

    public int    getLeadTime()                    { return leadTime; }
    public void   setLeadTime(int v)               { this.leadTime = v; }

    public int    getRating()                      { return rating; }
    public void   setRating(int v)                 { this.rating = v; }

    public String getStatus()                      { return status; }
    public void   setStatus(String v)              { this.status = v; }

    public String getCreatedDate()                 { return createdDate; }
    public void   setCreatedDate(String v)         { this.createdDate = v; }

    public String getLastUpdated()                 { return lastUpdated; }
    public void   setLastUpdated(String v)         { this.lastUpdated = v; }

    // ── toString (for debug logging) ──────────────────────────────────────────

    @Override
    public String toString() {
        return "Supplier{" +
               "id='"           + id            + '\'' +
               ", name='"       + supplierName  + '\'' +
               ", status='"     + status        + '\'' +
               ", rating="      + rating        +
               ", categories="  + categories    +
               '}';
    }
}

package com.smartgrocery.servlet;

import com.smartgrocery.model.Product;
import com.smartgrocery.model.User;
import com.smartgrocery.service.ProductService;
import com.smartgrocery.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/discounts")
public class DiscountServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() { productService = new ProductService(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) { res.sendRedirect("login.jsp"); return; }
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) { res.sendRedirect("dashboard.jsp?error=AccessDenied"); return; }

        List<Product> products = productService.getAllProducts();
        Map<String, double[]> discounts = loadDiscounts();
        req.setAttribute("products", products);
        req.setAttribute("discounts", discounts);
        req.getRequestDispatcher("discount-management.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) { res.sendRedirect("login.jsp"); return; }
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) { res.sendRedirect("dashboard.jsp"); return; }

        String[] ids    = req.getParameterValues("productId");
        String[] pcts   = req.getParameterValues("discountPct");
        String[] fixed  = req.getParameterValues("discountFixed");

        Map<String, double[]> discounts = new HashMap<>();
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                double pct = 0, fix = 0;
                try { pct  = Double.parseDouble(pcts[i]);  } catch (Exception ignored) {}
                try { fix  = Double.parseDouble(fixed[i]); } catch (Exception ignored) {}
                if (pct > 0 || fix > 0) discounts.put(ids[i], new double[]{pct, fix});
            }
        }
        saveDiscounts(discounts);
        res.sendRedirect("discounts?action=manage&msg=Saved");
    }

    /** discounts.txt format: productId|pct|fixed */
    public static Map<String, double[]> loadDiscounts() {
        Map<String, double[]> map = new HashMap<>();
        File f = new File(Constants.DATA_DIR + "discounts.txt");
        if (!f.exists()) return map;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 3) map.put(p[0], new double[]{Double.parseDouble(p[1]), Double.parseDouble(p[2])});
            }
        } catch (Exception ignored) {}
        return map;
    }

    private void saveDiscounts(Map<String, double[]> map) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.DATA_DIR + "discounts.txt", false))) {
            for (Map.Entry<String, double[]> e : map.entrySet()) {
                bw.write(e.getKey() + "|" + e.getValue()[0] + "|" + e.getValue()[1]);
                bw.newLine();
            }
        } catch (Exception ignored) {}
    }
}

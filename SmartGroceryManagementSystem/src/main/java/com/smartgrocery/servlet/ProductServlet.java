package com.smartgrocery.servlet;

import com.smartgrocery.model.NonPerishableProduct;
import com.smartgrocery.model.PerishableProduct;
import com.smartgrocery.model.Product;
import com.smartgrocery.model.User;
import com.smartgrocery.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                // ONLY ADMIN can add new products
                if (!"ADMIN".equals(user.getRole())) {
                    response.sendRedirect("dashboard.jsp?error=AccessDenied");
                    return;
                }
                showNewForm(request, response);
                break;
            case "edit":
                // ONLY ADMIN can edit products
                if (!"ADMIN".equals(user.getRole())) {
                    response.sendRedirect("dashboard.jsp?error=AccessDenied");
                    return;
                }
                showEditForm(request, response);
                break;
            case "delete":
                // ONLY ADMIN can delete products
                if (!"ADMIN".equals(user.getRole())) {
                    response.sendRedirect("dashboard.jsp?error=AccessDenied");
                    return;
                }
                deleteProduct(request, response);
                break;
            case "list":
            default:
                // EVERYONE can view products
                listProducts(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // ONLY ADMIN can save or update products
        if (!"ADMIN".equals(user.getRole())) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action) || "update".equals(action)) {
            saveOrUpdateProduct(request, response);
        } else {
            response.sendRedirect("products?action=list");
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        List<Product> list;

        if (search != null && !search.trim().isEmpty()) {
            list = productService.searchProducts(search);
            request.setAttribute("searchQuery", search);
        } else {
            list = productService.getAllProducts();
        }

        request.setAttribute("listProduct", list);
        request.getRequestDispatcher("product-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("product-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Product p = productService.getProductById(id);

        if (p != null) {
            request.setAttribute("productObj", p);
            if (p instanceof PerishableProduct) {
                request.setAttribute("specialField", ((PerishableProduct) p).getExpirationDate());
            } else if (p instanceof NonPerishableProduct) {
                request.setAttribute("specialField", ((NonPerishableProduct) p).getWarrantyMonths());
            }
            request.getRequestDispatcher("product-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("products?action=list&error=ProductNotFound");
        }
    }

    private void saveOrUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        String type = request.getParameter("type");
        String specialField = request.getParameter("specialField");

        String result = productService.saveOrUpdate(id, name, price, stock, type, specialField);

        if ("SUCCESS".equals(result)) {
            String msg = (id == null || id.trim().isEmpty()) ? "ProductCreated" : "ProductUpdated";
            response.sendRedirect("products?action=list&msg=" + msg);
        } else {
            // Repopulate form on error
            if (id != null && !id.isEmpty()) {
                request.setAttribute("productObj", productService.getProductById(id));
            }
            request.setAttribute("errorMessage", result);
            request.getRequestDispatcher("product-form.jsp").forward(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (productService.deleteProduct(id)) {
            response.sendRedirect("products?action=list&msg=ProductDeleted");
        } else {
            response.sendRedirect("products?action=list&error=DeleteFailed");
        }
    }
}
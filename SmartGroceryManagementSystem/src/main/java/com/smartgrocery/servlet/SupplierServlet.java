package com.smartgrocery.servlet;

import com.smartgrocery.model.Supplier;
import com.smartgrocery.model.User;
import com.smartgrocery.service.SupplierService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/suppliers")
public class SupplierServlet extends HttpServlet {

    private SupplierService supplierService;

    @Override
    public void init() throws ServletException {
        this.supplierService = new SupplierService();
    }

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkAdmin(request, response)) return;

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteSupplier(request, response);
                break;
            case "list":
            default:
                listSuppliers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkAdmin(request, response)) return;

        String action = request.getParameter("action");
        if ("save".equals(action) || "update".equals(action)) {
            saveOrUpdateSupplier(request, response);
        } else {
            response.sendRedirect("suppliers?action=list");
        }
    }

    private void listSuppliers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        List<Supplier> list;
        
        if (search != null && !search.trim().isEmpty()) {
            list = supplierService.searchSuppliers(search);
            request.setAttribute("searchQuery", search);
        } else {
            list = supplierService.getAllSuppliers();
        }
        
        request.setAttribute("listSupplier", list);
        request.getRequestDispatcher("supplier-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("supplier-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Supplier s = supplierService.getSupplierById(id);
        
        if (s != null) {
            request.setAttribute("supplierObj", s);
            request.getRequestDispatcher("supplier-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("suppliers?action=list&error=NotFound");
        }
    }

    private void saveOrUpdateSupplier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id"); // null if new
        String companyName = request.getParameter("companyName");
        String contactName = request.getParameter("contactName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        Supplier supplier = new Supplier(id, companyName, contactName, phone, email, address);
        String result = supplierService.saveOrUpdate(supplier);

        if ("SUCCESS".equals(result)) {
            String msg = (id == null || id.isEmpty()) ? "SupplierCreated" : "SupplierUpdated";
            response.sendRedirect("suppliers?action=list&msg=" + msg);
        } else {
            request.setAttribute("supplierObj", supplier);
            request.setAttribute("errorMessage", result);
            request.getRequestDispatcher("supplier-form.jsp").forward(request, response);
        }
    }

    private void deleteSupplier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (supplierService.deleteSupplier(id)) {
            response.sendRedirect("suppliers?action=list&msg=SupplierDeleted");
        } else {
            response.sendRedirect("suppliers?action=list&error=DeleteFailed");
        }
    }
}

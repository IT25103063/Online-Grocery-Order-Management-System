package com.smartgrocery.servlet;

import com.smartgrocery.model.Order;
import com.smartgrocery.model.Product;
import com.smartgrocery.model.User;
import com.smartgrocery.service.OrderService;
import com.smartgrocery.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private OrderService orderService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
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
        if (action == null) action = "history";

        switch (action) {
            case "new":
                showPlaceOrderForm(request, response);
                break;
            case "history":
                showOrderHistory(request, response, user);
                break;
            case "management":
                if ("ADMIN".equals(user.getRole())) {
                    showOrderManagement(request, response);
                } else {
                    response.sendRedirect("orders?action=history");
                }
                break;
            case "details":
                showOrderDetails(request, response);
                break;
            case "export":
                exportToCSV(request, response);
                break;
            default:
                response.sendRedirect("orders?action=history");
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
        String action = request.getParameter("action");

        if ("place".equals(action)) {
            placeOrder(request, response, user);
        } else if ("updateStatus".equals(action) && "ADMIN".equals(user.getRole())) {
            updateStatus(request, response);
        } else if ("bulkUpdate".equals(action) && "ADMIN".equals(user.getRole())) {
            bulkUpdateStatus(request, response);
        } else if ("cancel".equals(action)) {
            cancelOrder(request, response);
        } else {
            response.sendRedirect("orders?action=history");
        }
    }

    private void showPlaceOrderForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> availableProducts = productService.getAllProducts();
        availableProducts.removeIf(p -> p.getStock() <= 0);
        request.setAttribute("products", availableProducts);
        request.getRequestDispatcher("place-order.jsp").forward(request, response);
    }

    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("order-history.jsp").forward(request, response);
    }

    private void showOrderManagement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filter parameters
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");

        int page = 1;
        int size = 10;
        try { page = Integer.parseInt(pageStr); } catch (Exception e) {}
        try { size = Integer.parseInt(sizeStr); } catch (Exception e) {}

        // Get filtered orders
        List<Order> filteredOrders = orderService.searchOrders(search, status, null, null);

        // Get statistics
        Map<String, Object> stats = orderService.getOrderStatistics();

        // Pagination
        int totalOrders = filteredOrders.size();
        int totalPages = (int) Math.ceil((double) totalOrders / size);
        int start = (page - 1) * size;
        int end = Math.min(start + size, totalOrders);
        List<Order> pageOrders = start < totalOrders ? filteredOrders.subList(start, end) : List.of();

        request.setAttribute("orders", pageOrders);
        request.setAttribute("stats", stats);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", size);
        request.setAttribute("searchQuery", search);
        request.setAttribute("statusFilter", status);

        request.getRequestDispatcher("order-management.jsp").forward(request, response);
    }

    private void showOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Order order = orderService.getOrderById(id);

        if (order != null) {
            request.setAttribute("order", order);
            request.getRequestDispatcher("order-details-modal.jsp").forward(request, response);
        } else {
            response.sendRedirect("orders?action=management&error=OrderNotFound");
        }
    }

    private void exportToCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        List<Order> orders = orderService.searchOrders(search, status, null, null);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"orders_" +
                new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Order ID,Date,Customer ID,Type,Total,Status");

        for (Order o : orders) {
            writer.printf("%s,%s,%s,%s,%.2f,%s\n",
                    o.getId(), o.getOrderDate(), o.getUserId(),
                    o.getType(), o.calculateGrandTotal(), o.getStatus());
        }

        writer.flush();
    }

    private void bulkUpdateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] orderIds = request.getParameterValues("orderIds");
        String newStatus = request.getParameter("bulkStatus");

        if (orderIds != null && orderIds.length > 0 && newStatus != null) {
            boolean success = orderService.bulkUpdateStatus(Arrays.asList(orderIds), newStatus);
            if (success) {
                response.sendRedirect("orders?action=management&msg=BulkUpdateSuccess");
            } else {
                response.sendRedirect("orders?action=management&error=BulkUpdateFailed");
            }
        } else {
            response.sendRedirect("orders?action=management&error=NoOrdersSelected");
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String type = request.getParameter("orderType");
        String[] productIds = request.getParameterValues("productIds");
        String[] quantities = request.getParameterValues("quantities");

        String result = orderService.placeOrder(user.getId(), type, productIds, quantities);

        if ("SUCCESS".equals(result)) {
            List<Order> userOrders = orderService.getOrdersByUser(user.getId());
            Order lastOrder = userOrders.get(userOrders.size() - 1);
            double total = lastOrder.calculateGrandTotal();
            response.sendRedirect("payment.jsp?orderId=" + lastOrder.getId() + "&amount=Rs. " + total);
        } else {
            request.setAttribute("errorMessage", result);
            showPlaceOrderForm(request, response);
        }
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("orderId");
        String status = request.getParameter("status");

        boolean updated = orderService.updateOrderStatus(id, status);
        if (updated) {
            response.sendRedirect("orders?action=management&msg=StatusUpdated");
        } else {
            response.sendRedirect("orders?action=management&error=UpdateFailed");
        }
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("orderId");
        String source = request.getParameter("source");

        boolean cancelled = orderService.cancelOrder(id);

        String redirect = "history".equals(source) ? "history" : "management";
        if (cancelled) {
            response.sendRedirect("orders?action=" + redirect + "&msg=OrderCancelled");
        } else {
            response.sendRedirect("orders?action=" + redirect + "&error=CancelFailed");
        }
    }
}
package com.smartgrocery.dao;

import com.smartgrocery.model.DeliveryOrder;
import com.smartgrocery.model.Order;
import com.smartgrocery.model.OrderItem;
import com.smartgrocery.model.PickupOrder;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDao {

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        File file = new File(Constants.ORDERS_FILE);
        if (!file.exists()) return orders;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String id = parts[0];
                    String userId = parts[1];
                    String date = parts[2];
                    String status = parts[3];
                    String type = parts[4];
                    double shippingFee = Double.parseDouble(parts[5]);
                    String itemsStr = parts[6];

                    Order order;
                    if ("DELIVERY".equals(type)) {
                        order = new DeliveryOrder(id, userId, date, status, shippingFee);
                    } else {
                        order = new PickupOrder(id, userId, date, status);
                    }

                    // Parse items: prodId:name:qty:price,prodId:name:qty:price
                    if (!itemsStr.isEmpty()) {
                        String[] itemsArr = itemsStr.split(",");
                        for (String itemData : itemsArr) {
                            String[] iParts = itemData.split(":");
                            if (iParts.length == 4) {
                                order.addItem(new OrderItem(iParts[0], iParts[1], Integer.parseInt(iParts[2]), Double.parseDouble(iParts[3])));
                            }
                        }
                    }
                    orders.add(order);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order findById(String id) {
        return findAll().stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Order> findByUserId(String userId) {
        List<Order> userOrders = new ArrayList<>();
        for (Order o : findAll()) {
            if (o.getUserId().equals(userId)) {
                userOrders.add(o);
            }
        }
        return userOrders;
    }

    public boolean save(Order order) {
        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId("ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.ORDERS_FILE, true))) {
            bw.write(formatLine(order));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Order order) {
        List<Order> orders = findAll();
        boolean found = false;

        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(order.getId())) {
                orders.set(i, order);
                found = true;
                break;
            }
        }

        if (found) {
            return rewriteFile(orders);
        }
        return false;
    }

    private boolean rewriteFile(List<Order> orders) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.ORDERS_FILE, false))) {
            for (Order o : orders) {
                bw.write(formatLine(o));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatLine(Order o) {
        StringBuilder sb = new StringBuilder();
        double shipping = (o instanceof DeliveryOrder) ? ((DeliveryOrder) o).getShippingFee() : 0.0;
        
        sb.append(o.getId()).append("|")
          .append(o.getUserId()).append("|")
          .append(o.getOrderDate()).append("|")
          .append(o.getStatus()).append("|")
          .append(o.getType()).append("|")
          .append(shipping).append("|");

        List<OrderItem> items = o.getItems();
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            sb.append(item.getProductId()).append(":")
              .append(item.getProductName()).append(":")
              .append(item.getQuantity()).append(":")
              .append(item.getUnitPrice());
            if (i < items.size() - 1) sb.append(",");
        }

        return sb.toString();
    }
}

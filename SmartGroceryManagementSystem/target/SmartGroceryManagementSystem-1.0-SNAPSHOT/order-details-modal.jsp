<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty order}">
  <div class="row">
    <div class="col-md-6">
      <h6 class="fw-bold">Order Information</h6>
      <table class="table table-sm table-borderless">
        <tr><th width="40%">Order ID:</th><td>${order.id}</td></tr>
        <tr><th>Order Date:</th><td>${order.orderDate}</td></tr>
        <tr><th>Status:</th>
          <td><span class="badge bg-${order.status == 'PENDING' ? 'warning' : order.status == 'COMPLETED' ? 'success' : 'danger'}">${order.status}</span></td>
        </tr>
        <tr><th>Type:</th><td>${order.type}</td></tr>
        <c:if test="${order.type == 'DELIVERY'}">
          <tr><th>Shipping Fee:</th><td>Rs. ${order.shippingFee}</td></tr>
        </c:if>
      </table>
    </div>
    <div class="col-md-6">
      <h6 class="fw-bold">Customer Information</h6>
      <table class="table table-sm table-borderless">
        <tr><th width="40%">Customer ID:</th><td>${order.userId}</td></tr>
      </table>
    </div>
  </div>

  <h6 class="fw-bold mt-3">Order Items</h6>
  <table class="table table-sm">
    <thead class="table-light">
    <tr><th>Product</th><th>Quantity</th><th>Unit Price</th><th>Subtotal</th></tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${order.items}">
      <tr>
        <td>${item.productName}</td>
        <td>${item.quantity}</td>
        <td>Rs. ${item.unitPrice}</td>
        <td>Rs. ${item.subtotal}</td>
      </tr>
    </c:forEach>
    <tr class="table-light">
      <td colspan="3" class="text-end fw-bold">Subtotal:</td>
      <td>Rs. ${order.calculateSubtotal()}</td>
    </tr>
    <tr class="table-light">
      <td colspan="3" class="text-end fw-bold">Tax (5%):</td>
      <td>Rs. ${order.calculateTax()}</td>
    </tr>
    <tr class="table-success">
      <td colspan="3" class="text-end fw-bold">Grand Total:</td>
      <td class="fw-bold">Rs. ${order.calculateGrandTotal()}</td>
    </tr>
    </tbody>
  </table>
</c:if>
<c:if test="${empty order}">
  <div class="alert alert-danger">Order not found.</div>
</c:if>
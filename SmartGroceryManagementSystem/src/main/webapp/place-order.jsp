<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="Place New Order - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <h2 class="fw-bold mb-4">Place New Order</h2>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/orders" method="post" id="orderForm">
            <input type="hidden" name="action" value="place">

            <div class="row">
                <div class="col-lg-8">
                    <div class="card shadow-sm border-0 mb-4">
                        <div class="card-header bg-white pt-3 pb-2">
                            <h5 class="fw-bold">Available Products</h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table align-middle mb-0">
                                    <thead class="table-light">
                                    <tr>
                                        <th class="ps-3">Product Name</th>
                                        <th>Price (Rs.)</th>
                                        <th>Available Stock</th>
                                        <th class="text-end pe-3" style="width: 150px;">Quantity</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="prod" items="${products}">
                                        <tr>
                                            <td class="ps-3">
                                                <div class="fw-bold">${prod.name}</div>
                                                <div class="small text-muted">${prod.specialDetail}</div>
                                                <input type="hidden" name="productIds" value="${prod.id}" disabled class="pid-input">
                                            </td>
                                            <td class="fw-bold text-success">Rs. ${prod.price}</td>
                                            <td><span class="badge bg-success">${prod.stock}</span></td>
                                            <td class="text-end pe-3">
                                                <input type="number" class="form-control form-control-sm qty-input text-end"
                                                       name="quantities" min="0" max="${prod.stock}" value="0"
                                                       data-price="${prod.price}" onchange="updateCart()">
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty products}">
                                        <tr>
                                            <td colspan="4" class="text-center py-4 text-muted">No products currently available.</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card shadow-sm border-0 sticky-top" style="top: 80px;">
                        <div class="card-header bg-white pt-3 pb-2">
                            <h5 class="fw-bold">Order Summary</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-4">
                                <label class="form-label fw-bold">Order Type</label>
                                <select class="form-select" name="orderType" id="orderType" onchange="updateCart()" required>
                                    <option value="PICKUP">In-Store Pickup (Free)</option>
                                    <option value="DELIVERY">Delivery (Rs. 1,800)</option>
                                </select>
                            </div>

                            <hr>

                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Subtotal:</span>
                                <span class="fw-bold" id="subtotalDisplay">Rs. 0.00</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Tax (5%):</span>
                                <span class="fw-bold" id="taxDisplay">Rs. 0.00</span>
                            </div>
                            <div class="d-flex justify-content-between mb-3" id="deliveryFeeRow" style="display: none !important;">
                                <span class="text-muted">Delivery Fee:</span>
                                <span class="fw-bold">Rs. 1,800</span>
                            </div>

                            <hr>

                            <div class="d-flex justify-content-between mb-4">
                                <span class="fw-bold fs-5">Grand Total:</span>
                                <span class="fw-bold fs-5 text-success" id="totalDisplay">Rs. 0.00</span>
                            </div>

                            <button type="submit" class="btn btn-success w-100 py-2 fw-bold" id="submitBtn" disabled>
                                Place Order
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</main>

<script>
    function updateCart() {
        let subtotal = 0;
        let hasItems = false;

        const qtyInputs = document.querySelectorAll('.qty-input');
        const pidInputs = document.querySelectorAll('.pid-input');

        qtyInputs.forEach((input, index) => {
            let qty = parseInt(input.value) || 0;
            if (qty > 0) {
                let price = parseFloat(input.getAttribute('data-price'));
                subtotal += (qty * price);
                hasItems = true;
                pidInputs[index].disabled = false;
            } else {
                pidInputs[index].disabled = true;
            }
        });

        let tax = subtotal * 0.05;
        let orderType = document.getElementById('orderType').value;
        let fee = (orderType === 'DELIVERY') ? 1800 : 0;
        let total = subtotal + tax + fee;

        document.getElementById('subtotalDisplay').innerText = 'Rs. ' + subtotal.toFixed(2);
        document.getElementById('taxDisplay').innerText = 'Rs. ' + tax.toFixed(2);
        document.getElementById('totalDisplay').innerText = 'Rs. ' + total.toFixed(2);

        let feeRow = document.getElementById('deliveryFeeRow');
        if(orderType === 'DELIVERY' && hasItems) {
            feeRow.style.setProperty('display', 'flex', 'important');
        } else {
            feeRow.style.setProperty('display', 'none', 'important');
        }

        document.getElementById('submitBtn').disabled = !hasItems;
    }
</script>

<jsp:include page="/WEB-INF/components/footer.jsp" />
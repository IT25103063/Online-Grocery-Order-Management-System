<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="My Order History - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">My Order History</h2>
            <a href="${pageContext.request.contextPath}/orders?action=new" class="btn btn-primary">
                <i class="fa-solid fa-cart-plus me-2"></i>Place New Order
            </a>
        </div>

        <c:if test="${param.msg == 'OrderPlaced'}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fa-solid fa-check-circle me-2"></i>Order placed successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'OrderCancelled'}">
            <div class="alert alert-warning alert-dismissible fade show">
                <i class="fa-solid fa-ban me-2"></i>Order cancelled successfully.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <c:forEach var="order" items="${orders}">
                <div class="col-md-6 mb-4">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center pt-3 pb-2 border-bottom-0">
                            <div>
                                <h6 class="fw-bold mb-0">${order.id}</h6>
                                <small class="text-muted">${order.orderDate}</small>
                            </div>
                            <div>
                                <c:choose>
                                    <c:when test="${order.status == 'PENDING'}">
                                        <span class="badge bg-warning text-dark">Pending</span>
                                    </c:when>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <span class="badge bg-success">Completed</span>
                                    </c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">
                                        <span class="badge bg-danger">Cancelled</span>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                        <div class="card-body bg-light rounded m-2 p-3">
                            <h6 class="text-muted small fw-bold text-uppercase mb-3 border-bottom pb-2">Items</h6>
                            <ul class="list-unstyled mb-0">
                                <c:forEach var="item" items="${order.items}">
                                    <li class="d-flex justify-content-between mb-1">
                                        <span>${item.quantity}x ${item.productName}</span>
                                        <span class="fw-bold">Rs. ${item.subtotal}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="card-footer bg-white border-top-0 d-flex justify-content-between align-items-center pb-3">
                            <div>
                                <span class="badge bg-secondary me-2">${order.type}</span>
                                <span class="fw-bold fs-5 text-success">Total: Rs. ${order.calculateGrandTotal()}</span>
                            </div>

                            <c:if test="${order.status == 'PENDING'}">
                                <form action="${pageContext.request.contextPath}/orders" method="post" class="m-0">
                                    <input type="hidden" name="action" value="cancel">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <input type="hidden" name="source" value="history">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('Cancel this order?');">Cancel Order</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty orders}">
                <div class="col-12 text-center py-5">
                    <i class="fa-solid fa-box-open fa-4x text-muted mb-3"></i>
                    <h5 class="text-muted">You haven't placed any orders yet.</h5>
                </div>
            </c:if>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />
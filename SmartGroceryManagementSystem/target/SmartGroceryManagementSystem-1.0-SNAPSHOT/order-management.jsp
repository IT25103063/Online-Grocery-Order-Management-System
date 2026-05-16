<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="pageTitle" value="Order Management - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">Order Management</h2>
            <a href="${pageContext.request.contextPath}/orders?action=export${not empty searchQuery ? '&search=' += searchQuery : ''}${not empty statusFilter ? '&status=' += statusFilter : ''}"
               class="btn btn-success">
                <i class="fa-solid fa-download me-2"></i>Export to CSV
            </a>
        </div>

        <!-- ========== STATISTICS CARDS ========== -->
        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="card text-white border-0 shadow-sm" style="background: linear-gradient(135deg, #3b82f6, #2563eb);">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-white-50 mb-1">Total Orders</h6>
                                <h2 class="fw-bold mb-0">${stats.total}</h2>
                            </div>
                            <i class="fa-solid fa-shopping-cart fa-2x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white border-0 shadow-sm" style="background: linear-gradient(135deg, #f59e0b, #d97706);">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-white-50 mb-1">Pending</h6>
                                <h2 class="fw-bold mb-0">${stats.pending}</h2>
                            </div>
                            <i class="fa-solid fa-clock fa-2x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white border-0 shadow-sm" style="background: linear-gradient(135deg, #10b981, #059669);">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-white-50 mb-1">Completed</h6>
                                <h2 class="fw-bold mb-0">${stats.completed}</h2>
                            </div>
                            <i class="fa-solid fa-check-circle fa-2x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white border-0 shadow-sm" style="background: linear-gradient(135deg, #ef4444, #dc2626);">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-white-50 mb-1">Revenue</h6>
                                <h2 class="fw-bold mb-0">Rs. ${stats.revenue}</h2>
                            </div>
                            <i class="fa-solid fa- rupee-sign fa-2x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- ========== SEARCH & FILTER BAR ========== -->
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body">
                <form method="get" action="${pageContext.request.contextPath}/orders" class="row g-3">
                    <input type="hidden" name="action" value="management">
                    <div class="col-md-4">
                        <input type="text" class="form-control" name="search" placeholder="Search by Order ID or Customer ID" value="${searchQuery}">
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="status">
                            <option value="ALL" ${statusFilter == 'ALL' ? 'selected' : ''}>All Status</option>
                            <option value="PENDING" ${statusFilter == 'PENDING' ? 'selected' : ''}>Pending</option>
                            <option value="COMPLETED" ${statusFilter == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                            <option value="CANCELLED" ${statusFilter == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select class="form-select" name="size">
                            <option value="10" ${pageSize == 10 ? 'selected' : ''}>10 per page</option>
                            <option value="25" ${pageSize == 25 ? 'selected' : ''}>25 per page</option>
                            <option value="50" ${pageSize == 50 ? 'selected' : ''}>50 per page</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fa-solid fa-search me-2"></i>Search
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- ========== BULK ACTIONS ========== -->
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/orders" class="row g-3 align-items-center" id="bulkForm">
                    <input type="hidden" name="action" value="bulkUpdate">
                    <div class="col-md-3">
                        <select name="bulkStatus" class="form-select" required>
                            <option value="">Select Action</option>
                            <option value="COMPLETED">Mark as Completed</option>
                            <option value="CANCELLED">Mark as Cancelled</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-warning" onclick="return confirmBulkUpdate()">
                            <i class="fa-solid fa-layer-group me-2"></i>Apply to Selected
                        </button>
                    </div>
                    <div class="col-md-6 text-end">
                        <span id="selectedCount" class="text-muted">0 orders selected</span>
                    </div>
                </form>
            </div>
        </div>

        <!-- ========== ALERTS ========== -->
        <c:if test="${param.msg == 'StatusUpdated'}">
            <div class="alert alert-success alert-dismissible fade show">Status updated successfully!<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.msg == 'OrderCancelled'}">
            <div class="alert alert-warning alert-dismissible fade show">Order cancelled successfully!<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.msg == 'BulkUpdateSuccess'}">
            <div class="alert alert-success alert-dismissible fade show">Bulk update completed successfully!<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>

        <!-- ========== ORDERS TABLE ========== -->
        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <th width="40"><input type="checkbox" id="selectAll" onclick="toggleAll(this)"></th>
                            <th>Order ID</th>
                            <th>Date</th>
                            <th>Customer ID</th>
                            <th>Type</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td><input type="checkbox" class="orderCheckbox" name="orderIds" value="${order.id}" form="bulkForm"></td>
                                <td class="fw-bold">${order.id}</td>
                                <td>${order.orderDate}</td>
                                <td>${order.userId}</td>
                                <td><span class="badge bg-secondary">${order.type}</span></td>
                                <td class="fw-bold text-success">Rs. ${order.calculateGrandTotal()}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.status == 'PENDING'}"><span class="badge bg-warning text-dark">Pending</span></c:when>
                                        <c:when test="${order.status == 'COMPLETED'}"><span class="badge bg-success">Completed</span></c:when>
                                        <c:otherwise><span class="badge bg-danger">Cancelled</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <button class="btn btn-sm btn-outline-info me-1" onclick="showOrderDetails('${order.id}')">
                                        <i class="fa-solid fa-eye"></i>
                                    </button>
                                    <c:if test="${order.status == 'PENDING'}">
                                        <form action="${pageContext.request.contextPath}/orders" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="updateStatus">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <select name="status" class="form-select form-select-sm d-inline-block" style="width: auto;">
                                                <option value="COMPLETED">Complete</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-success">Update</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/orders" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <input type="hidden" name="source" value="management">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('Cancel this order?');">Cancel</button>
                                        </form>
                                    </c:if>
                                    <c:if test="${order.status != 'PENDING'}">
                                        <span class="text-muted small">No actions</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty orders}">
                            <tr>
                                <td colspan="8" class="text-center py-4">No orders found.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- ========== PAGINATION ========== -->
        <c:if test="${totalPages > 1}">
            <nav class="mt-4">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="?action=management&page=${currentPage-1}&size=${pageSize}&search=${searchQuery}&status=${statusFilter}">Previous</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="?action=management&page=${i}&size=${pageSize}&search=${searchQuery}&status=${statusFilter}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="?action=management&page=${currentPage+1}&size=${pageSize}&search=${searchQuery}&status=${statusFilter}">Next</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
</main>

<!-- Order Details Modal -->
<div class="modal fade" id="orderDetailsModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Order Details</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="orderDetailsContent">
                <div class="text-center py-4">
                    <div class="spinner-border text-primary" role="status"></div>
                    <p class="mt-2">Loading...</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function toggleAll(source) {
        document.querySelectorAll('.orderCheckbox').forEach(cb => cb.checked = source.checked);
        updateSelectedCount();
    }

    function updateSelectedCount() {
        let count = document.querySelectorAll('.orderCheckbox:checked').length;
        document.getElementById('selectedCount').innerText = count + ' orders selected';
    }

    function confirmBulkUpdate() {
        let count = document.querySelectorAll('.orderCheckbox:checked').length;
        if (count === 0) {
            alert('Please select at least one order.');
            return false;
        }
        return confirm('Update ' + count + ' orders?');
    }

    function showOrderDetails(orderId) {
        fetch('${pageContext.request.contextPath}/orders?action=details&id=' + orderId)
            .then(response => response.text())
            .then(data => {
                document.getElementById('orderDetailsContent').innerHTML = data;
                new bootstrap.Modal(document.getElementById('orderDetailsModal')).show();
            });
    }

    document.querySelectorAll('.orderCheckbox').forEach(cb => {
        cb.addEventListener('change', updateSelectedCount);
    });
</script>

<jsp:include page="/WEB-INF/components/footer.jsp" />
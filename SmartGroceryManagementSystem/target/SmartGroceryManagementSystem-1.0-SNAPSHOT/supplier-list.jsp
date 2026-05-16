<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="pageTitle" value="Supplier Management - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">Supplier Management</h2>
            <a href="${pageContext.request.contextPath}/suppliers?action=new" class="btn btn-primary">
                <i class="fa-solid fa-plus me-2"></i>Add New Supplier
            </a>
        </div>

        <%-- Alerts --%>
        <c:if test="${param.msg == 'SupplierCreated'}">
            <div class="alert alert-success alert-dismissible fade show"><i class="fa-solid fa-check-circle me-2"></i>Supplier created successfully!<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.msg == 'SupplierUpdated'}">
            <div class="alert alert-success alert-dismissible fade show"><i class="fa-solid fa-check-circle me-2"></i>Supplier updated successfully!<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.msg == 'SupplierDeleted'}">
            <div class="alert alert-warning alert-dismissible fade show"><i class="fa-solid fa-trash me-2"></i>Supplier deleted successfully!<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>

        <%-- Search Bar --%>
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body bg-light rounded">
                <form action="${pageContext.request.contextPath}/suppliers" method="get" class="row g-3 align-items-center">
                    <input type="hidden" name="action" value="list">
                    <div class="col-md-10">
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0"><i class="fa-solid fa-search text-muted"></i></span>
                            <input type="text" class="form-control border-start-0" name="search" value="${searchQuery}" placeholder="Search by Company, Contact Name, or Email...">
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100">Search</button>
                    </div>
                </form>
            </div>
        </div>

        <%-- Data Table --%>
        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-4">ID</th>
                                <th>Company Name</th>
                                <th>Contact Person</th>
                                <th>Contact Info</th>
                                <th>Address</th>
                                <th class="text-end pe-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="supplier" items="${listSupplier}">
                                <tr>
                                    <td class="ps-4 text-muted font-monospace small">${supplier.id}</td>
                                    <td class="fw-bold"><i class="fa-solid fa-truck text-primary me-2"></i>${supplier.companyName}</td>
                                    <td>${supplier.contactName}</td>
                                    <td>
                                        <div class="small"><i class="fa-solid fa-phone me-1 text-muted"></i> ${supplier.phone}</div>
                                        <div class="small"><i class="fa-solid fa-envelope me-1 text-muted"></i> <a href="mailto:${supplier.email}" class="text-decoration-none">${supplier.email}</a></div>
                                    </td>
                                    <td class="text-muted small">${supplier.address}</td>
                                    <td class="text-end pe-4">
                                        <a href="${pageContext.request.contextPath}/suppliers?action=edit&id=${supplier.id}" class="btn btn-sm btn-outline-primary me-2">
                                            <i class="fa-solid fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/suppliers?action=delete&id=${supplier.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this supplier?');">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listSupplier}">
                                <tr>
                                    <td colspan="6" class="text-center py-5 text-muted">
                                        <i class="fa-solid fa-truck-ramp-box fa-3x mb-3 d-block"></i>
                                        No suppliers found.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

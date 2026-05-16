<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="Product Management - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">Inventory Management</h2>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/products?action=new" class="btn btn-success">
                    <i class="fa-solid fa-plus me-2"></i>Add New Product
                </a>
            </c:if>
        </div>

        <c:if test="${param.msg == 'ProductCreated'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-check-circle me-2"></i>Product created successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'ProductUpdated'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-check-circle me-2"></i>Product updated successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'ProductDeleted'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-trash me-2"></i>Product deleted successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body bg-light rounded">
                <form action="${pageContext.request.contextPath}/products" method="get" class="row g-3 align-items-center">
                    <input type="hidden" name="action" value="list">
                    <div class="col-md-10">
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0"><i class="fa-solid fa-search text-muted"></i></span>
                            <input type="text" class="form-control border-start-0" name="search" value="${searchQuery}" placeholder="Search by ID, name, or type...">
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100">Search</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <th class="ps-4">ID</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Price (Rs.)</th>
                            <th>Stock</th>
                            <th>Details</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="prod" items="${listProduct}">
                            <tr>
                                <td class="ps-4 text-muted font-monospace small">${prod.id}</td>
                                <td class="fw-bold">${prod.name}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${prod.type == 'PERISHABLE'}">
                                            <span class="badge bg-warning text-dark"><i class="fa-solid fa-apple-whole me-1"></i> Perishable</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary"><i class="fa-solid fa-box-open me-1"></i> Non-Perishable</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="fw-bold text-success">Rs. ${prod.price}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${prod.stock <= 10}">
                                            <span class="text-danger fw-bold"><i class="fa-solid fa-circle-exclamation me-1"></i>${prod.stock}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-success">${prod.stock}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-muted small">${prod.specialDetail}</td>
                                <td class="text-end pe-4">
                                    <a href="${pageContext.request.contextPath}/reviews?action=product&id=${prod.id}" class="btn btn-sm btn-outline-warning me-2" title="View Reviews">
                                        <i class="fa-solid fa-star"></i>
                                    </a>
                                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                        <a href="${pageContext.request.contextPath}/products?action=edit&id=${prod.id}" class="btn btn-sm btn-outline-primary me-2">
                                            <i class="fa-solid fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/products?action=delete&id=${prod.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this product?');">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listProduct}">
                            <tr>
                                <td colspan="7" class="text-center py-4 text-muted">
                                    <i class="fa-solid fa-box fa-3x mb-3 d-block"></i>
                                    No products found.
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
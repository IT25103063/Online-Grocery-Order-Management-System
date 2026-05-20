<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="Product Management - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<style>
    .category-card {
        cursor: pointer;
        border: 2px solid transparent;
        border-radius: 16px;
        transition: all 0.2s ease;
        text-decoration: none;
        display: block;
        padding: 1.2rem 0.8rem;
        text-align: center;
        background: #fff;
        box-shadow: 0 2px 8px rgba(0,0,0,0.07);
    }
    .category-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 6px 20px rgba(0,0,0,0.12);
        text-decoration: none;
    }
    .category-card.active {
        border-color: #198754;
        background: #f0fdf4;
        box-shadow: 0 4px 16px rgba(25,135,84,0.18);
    }
    .category-card .cat-icon { font-size: 2rem; display: block; margin-bottom: 0.4rem; }
    .category-card .cat-name { font-size: 0.78rem; font-weight: 600; color: #444; }
    .category-card.active .cat-name { color: #198754; }

    .stock-badge-low  { color: #dc3545; font-weight: 700; }
    .stock-badge-ok   { color: #198754; }

    .cat-pill {
        font-size: 0.72rem;
        padding: 3px 10px;
        border-radius: 20px;
        font-weight: 600;
    }
    .table thead th { font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.04em; color: #666; }
    .table tbody td { vertical-align: middle; }
</style>

<main class="main-content">
    <div class="container-fluid">

        <%-- Header row --%>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold mb-0">Inventory Management</h2>
                <p class="text-muted mb-0 small">Browse and manage products by category</p>
            </div>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/products?action=new" class="btn btn-success px-4">
                    <i class="fa-solid fa-plus me-2"></i>Add Product
                </a>
            </c:if>
        </div>

        <%-- Alerts --%>
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
                <i class="fa-solid fa-trash me-2"></i>Product deleted.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <%-- Category Cards --%>
        <div class="row g-3 mb-4">
            <c:set var="sel" value="${param.category}" />

            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list"
                   class="category-card ${empty sel ? 'active' : ''}">
                    <span class="cat-icon">🛒</span>
                    <span class="cat-name">All Products</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Milk+%26+Dairy"
                   class="category-card ${sel == 'Milk & Dairy' ? 'active' : ''}">
                    <span class="cat-icon">🥛</span>
                    <span class="cat-name">Milk &amp; Dairy</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Baby"
                   class="category-card ${sel == 'Baby' ? 'active' : ''}">
                    <span class="cat-icon">🍼</span>
                    <span class="cat-name">Baby</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Vegetables"
                   class="category-card ${sel == 'Vegetables' ? 'active' : ''}">
                    <span class="cat-icon">🥦</span>
                    <span class="cat-name">Vegetables</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Fruits"
                   class="category-card ${sel == 'Fruits' ? 'active' : ''}">
                    <span class="cat-icon">🍎</span>
                    <span class="cat-name">Fruits</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Meat+%26+Fish"
                   class="category-card ${sel == 'Meat & Fish' ? 'active' : ''}">
                    <span class="cat-icon">🥩</span>
                    <span class="cat-name">Meat &amp; Fish</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Ice+Cream"
                   class="category-card ${sel == 'Ice Cream' ? 'active' : ''}">
                    <span class="cat-icon">🍦</span>
                    <span class="cat-name">Ice Cream</span>
                </a>
            </div>
            <div class="col-6 col-sm-4 col-md-2">
                <a href="${pageContext.request.contextPath}/products?action=list&category=Bakery+%26+Staples"
                   class="category-card ${sel == 'Bakery & Staples' ? 'active' : ''}">
                    <span class="cat-icon">🍞</span>
                    <span class="cat-name">Bakery &amp; Staples</span>
                </a>
            </div>
        </div>

        <%-- Product Table --%>
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white border-0 py-3 px-4 d-flex align-items-center justify-content-between">
                <span class="fw-semibold text-dark">
                    <c:choose>
                        <c:when test="${not empty sel}">${sel}</c:when>
                        <c:otherwise>All Products</c:otherwise>
                    </c:choose>
                    <span class="badge bg-light text-secondary ms-2">${listProduct.size()} items</span>
                </span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-4">ID</th>
                                <th>Name</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Stock</th>
                                <th class="text-end pe-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="prod" items="${listProduct}">
                            <tr>
                                <td class="ps-4 text-muted font-monospace small">${prod.id}</td>
                                <td>
                                    <span class="fw-semibold">${prod.name}</span>
                                    <br>
                                    <span class="text-muted" style="font-size:0.75rem">${prod.specialDetail}</span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${prod.category == 'Milk & Dairy'}">
                                            <span class="cat-pill bg-info bg-opacity-10 text-info">🥛 ${prod.category}</span>
                                        </c:when>
                                        <c:when test="${prod.category == 'Baby'}">
                                            <span class="cat-pill bg-pink bg-opacity-10" style="background:#fce4ec;color:#c2185b;">🍼 ${prod.category}</span>
                                        </c:when>
                                        <c:when test="${prod.category == 'Vegetables'}">
                                            <span class="cat-pill bg-success bg-opacity-10 text-success">🥦 ${prod.category}</span>
                                        </c:when>
                                        <c:when test="${prod.category == 'Fruits'}">
                                            <span class="cat-pill bg-warning bg-opacity-10 text-warning">🍎 ${prod.category}</span>
                                        </c:when>
                                        <c:when test="${prod.category == 'Meat & Fish'}">
                                            <span class="cat-pill bg-danger bg-opacity-10 text-danger">🥩 ${prod.category}</span>
                                        </c:when>
                                        <c:when test="${prod.category == 'Ice Cream'}">
                                            <span class="cat-pill" style="background:#e3f2fd;color:#1565c0;">🍦 ${prod.category}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="cat-pill bg-secondary bg-opacity-10 text-secondary">🍞 ${prod.category}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="fw-bold text-success">Rs. ${prod.price}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${prod.stock <= 10}">
                                            <span class="stock-badge-low"><i class="fa-solid fa-circle-exclamation me-1"></i>${prod.stock}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock-badge-ok">${prod.stock}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end pe-4">
                                    <a href="${pageContext.request.contextPath}/reviews?action=product&id=${prod.id}"
                                       class="btn btn-sm btn-outline-warning me-1" title="Reviews">
                                        <i class="fa-solid fa-star"></i>
                                    </a>
                                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                        <a href="${pageContext.request.contextPath}/products?action=edit&id=${prod.id}"
                                           class="btn btn-sm btn-outline-primary me-1">
                                            <i class="fa-solid fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/products?action=delete&id=${prod.id}"
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Delete this product?');">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listProduct}">
                            <tr>
                                <td colspan="6" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-box fa-3x mb-3 d-block opacity-25"></i>
                                    No products found in this category.
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

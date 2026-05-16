<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav id="sidebar" class="sidebar bg-dark">
    <div class="position-sticky">
        <ul class="nav flex-column mt-3">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/dashboard.jsp">
                    <i class="fa-solid fa-gauge"></i> Dashboard
                </a>
            </li>

            <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                <span>Products & Orders</span>
            </h6>

            <!-- Products - Everyone can VIEW -->
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/products?action=list">
                    <i class="fa-solid fa-box"></i> Products
                </a>
            </li>

            <!-- Order link - Different for admin vs customer -->
            <li class="nav-item">
                <c:choose>
                    <c:when test="${sessionScope.user.role == 'ADMIN'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/orders?action=management">
                            <i class="fa-solid fa-clipboard-list"></i> Order Management
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="${pageContext.request.contextPath}/orders?action=history">
                            <i class="fa-solid fa-shopping-bag"></i> My Orders
                        </a>
                    </c:otherwise>
                </c:choose>
            </li>

            <!-- ADMIN ONLY Sections -->
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                    <span>Management</span>
                </h6>

                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/users?action=list">
                        <i class="fa-solid fa-users"></i> User Management
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/suppliers">
                        <i class="fa-solid fa-truck"></i> Suppliers
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/reviews?action=management">
                        <i class="fa-solid fa-star-half-stroke"></i> Review Moderation
                    </a>
                </li>
            </c:if>

            <!-- Write Review - Everyone can do -->
            <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                <span>Community</span>
            </h6>

            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/reviews?action=write">
                    <i class="fa-solid fa-star"></i> Write a Review
                </a>
            </li>

            <!-- SUPER ADMIN ONLY Section -->
            <c:if test="${sessionScope.user.role == 'ADMIN' && sessionScope.user.adminCode == 'SUPER_ADMIN'}">
                <hr class="text-white-50 my-3">
                <h6 class="sidebar-heading px-3 mt-4 mb-1 text-danger text-uppercase fw-bold">
                    <span>Security</span>
                </h6>
                <li class="nav-item">
                    <a class="nav-link text-danger" href="${pageContext.request.contextPath}/admins?action=dashboard">
                        <i class="fa-solid fa-shield-halved"></i> Security Hub
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-warning" href="${pageContext.request.contextPath}/admin-approvals">
                        <i class="fa-solid fa-user-clock"></i> Admin Approvals
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>
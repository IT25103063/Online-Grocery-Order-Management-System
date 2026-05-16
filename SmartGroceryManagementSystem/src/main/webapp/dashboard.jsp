<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="Dashboard - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold mb-0 text-dark">Welcome back, ${sessionScope.user.username}! <span class="fs-4">👋</span></h2>
                <p class="text-muted mb-0">Here's what's happening in your grocery system today.</p>
            </div>
            <div>
                <span class="badge bg-primary fs-6 px-3 py-2 rounded-pill">
                    <i class="fa-solid fa-calendar-day me-2"></i><%= new java.text.SimpleDateFormat("EEEE, MMMM d").format(new java.util.Date()) %>
                </span>
            </div>
        </div>

        <c:if test="${param.error == 'AccessDenied'}">
            <div class="alert alert-danger shadow-sm border-0 alert-dismissible fade show">
                <i class="fa-solid fa-shield-halved me-2"></i><strong>Security Alert:</strong> You do not have permission to access that module.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Stat Cards -->
        <div class="row g-4 mb-4">
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <div class="col-xl-3 col-md-6">
                    <div class="card h-100 bg-primary text-white" style="background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="text-uppercase fw-semibold text-white-50 mb-1">System Health</h6>
                                    <h2 class="fw-bold mb-0">Online</h2>
                                </div>
                                <div class="bg-white bg-opacity-25 p-3 rounded-circle">
                                    <i class="fa-solid fa-server fa-2x"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="col-xl-3 col-md-6">
                <div class="card h-100 text-white" style="background: linear-gradient(135deg, #10b981 0%, #059669 100%);">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-uppercase fw-semibold text-white-50 mb-1">Products</h6>
                                <h2 class="fw-bold mb-0">Catalog</h2>
                            </div>
                            <div class="bg-white bg-opacity-25 p-3 rounded-circle">
                                <i class="fa-solid fa-box-open fa-2x"></i>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/products" class="text-white text-decoration-none mt-3 d-block small fw-semibold">
                            Browse Inventory <i class="fa-solid fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-xl-3 col-md-6">
                <div class="card h-100 text-white" style="background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-uppercase fw-semibold text-white-50 mb-1">My Orders</h6>
                                <h2 class="fw-bold mb-0">Track</h2>
                            </div>
                            <div class="bg-white bg-opacity-25 p-3 rounded-circle">
                                <i class="fa-solid fa-shopping-cart fa-2x"></i>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/orders" class="text-white text-decoration-none mt-3 d-block small fw-semibold">
                            View Order History <i class="fa-solid fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                </div>
            </div>

            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <div class="col-xl-3 col-md-6">
                    <div class="card h-100 text-white" style="background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 class="text-uppercase fw-semibold text-white-50 mb-1">Users</h6>
                                    <h2 class="fw-bold mb-0">Manage</h2>
                                </div>
                                <div class="bg-white bg-opacity-25 p-3 rounded-circle">
                                    <i class="fa-solid fa-users fa-2x"></i>
                                </div>
                            </div>
                            <a href="${pageContext.request.contextPath}/users" class="text-white text-decoration-none mt-3 d-block small fw-semibold">
                                View Accounts <i class="fa-solid fa-arrow-right ms-1"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- System Highlights & Profile -->
        <div class="row">
            <div class="col-lg-8 mb-4">
                <div class="card h-100 border-0 shadow-sm">
                    <div class="card-header bg-white border-bottom py-3">
                        <h5 class="fw-bold mb-0"><i class="fa-solid fa-bolt text-warning me-2"></i>System Highlights</h5>
                    </div>
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <div class="p-4 bg-light rounded border text-center h-100">
                                    <i class="fa-solid fa-truck-fast fa-3x text-primary mb-3 d-block"></i>
                                    <h5 class="fw-bold">Fast Delivery</h5>
                                    <p class="text-muted small mb-0">Order tracking is live for all delivery orders in the system.</p>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="p-4 bg-light rounded border text-center h-100">
                                    <i class="fa-solid fa-shield-check fa-3x text-success mb-3 d-block"></i>
                                    <h5 class="fw-bold">Secure Data</h5>
                                    <p class="text-muted small mb-0">Encapsulated file persistence ensuring your data remains uncorrupted.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 mb-4">
                <div class="card h-100 border-0 shadow-sm">
                    <div class="card-header bg-white border-bottom py-3">
                        <h5 class="fw-bold mb-0"><i class="fa-solid fa-user-circle text-primary me-2"></i>My Profile</h5>
                    </div>
                    <div class="card-body text-center pt-4">
                        <div class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center mb-3" style="width: 80px; height: 80px; font-size: 2rem;">
                            ${sessionScope.user.username.substring(0, 1).toUpperCase()}
                        </div>
                        <h4 class="fw-bold mb-1">${sessionScope.user.username}</h4>
                        <p class="text-muted mb-3">${sessionScope.user.email}</p>
                        <span class="badge bg-${sessionScope.user.role == 'ADMIN' ? 'danger' : 'success'} px-3 py-2 rounded-pill">
                            <i class="fa-solid fa-${sessionScope.user.role == 'ADMIN' ? 'crown' : 'user'} me-1"></i>
                            ${sessionScope.user.role}
                        </span>
                        <hr class="my-4">
                        <a href="profile.jsp" class="btn btn-outline-primary w-100">Edit Profile</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />
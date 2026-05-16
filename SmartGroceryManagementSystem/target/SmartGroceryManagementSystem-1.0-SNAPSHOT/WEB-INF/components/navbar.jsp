<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm mb-4">    <div class="container-fluid">
        <!-- Sidebar Toggle on Mobile -->
        <button class="navbar-toggler me-2 d-md-none" type="button" id="sidebarToggle">
            <span class="navbar-toggler-icon"></span>
        </button>

        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard.jsp">
            <i class="fa-solid fa-basket-shopping me-2"></i>Smart Grocery
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fa-solid fa-user-circle me-1"></i>
                        ${not empty sessionScope.user ? sessionScope.user.username : 'Admin User'}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile.jsp"><i class="fa-solid fa-id-badge me-2"></i>Profile</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fa-solid fa-cog me-2"></i>Settings</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt me-2"></i>Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
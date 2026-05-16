<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Session Check --%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="User Management - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">User Management</h2>
            <!-- ADD NEW USER BUTTON - ADMIN ONLY -->
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/users?action=new" class="btn btn-success">
                    <i class="fa-solid fa-plus me-2"></i>Add New User
                </a>
            </c:if>
        </div>

        <%-- Alerts --%>
        <c:if test="${param.msg == 'UserCreated'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-check-circle me-2"></i>User created successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'UserUpdated'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-check-circle me-2"></i>User updated successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'UserDeleted'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-trash me-2"></i>User deleted successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <%-- Search Bar --%>
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body bg-light rounded">
                <form action="${pageContext.request.contextPath}/users" method="get" class="row g-3 align-items-center">
                    <input type="hidden" name="action" value="list">
                    <div class="col-md-10">
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0"><i class="fa-solid fa-search text-muted"></i></span>
                            <input type="text" class="form-control border-start-0" id="search" name="search" value="${searchQuery}" placeholder="Search by username, email, or role...">
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
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="user" items="${listUser}">
                        <tr>
                            <td class="ps-4 text-muted font-monospace small">${user.id}</td>
                            <td class="fw-bold">${user.username}</td>
                            <td>${user.email}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.role == 'ADMIN'}">
                                        <span class="badge bg-danger">Admin</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-primary">Customer</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}" class="btn btn-sm btn-outline-primary me-2" data-bs-toggle="tooltip" title="Edit User">
                                    <i class="fa-solid fa-edit"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/users?action=delete&id=${user.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure you want to delete this user?');" data-bs-toggle="tooltip" title="Delete User">
                                    <i class="fa-solid fa-trash"></i>
                                </a>
                            </td>
                        <tr>
                            </c:forEach>
                            <c:if test="${empty listUser}">
                        <tr>
                            <td colspan="5" class="text-center py-4 text-muted">
                                <i class="fa-solid fa-inbox fa-3x mb-3 d-block"></i>
                                No users found.
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN' || sessionScope.user.adminCode != 'SUPER_ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="pageTitle" value="Administrator Management - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">
                <a href="${pageContext.request.contextPath}/admins?action=dashboard" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
                Administrator Accounts
            </h2>
            <a href="${pageContext.request.contextPath}/admins?action=new" class="btn btn-danger">
                <i class="fa-solid fa-plus me-2"></i>Add New Admin
            </a>
        </div>

        <c:if test="${param.msg == 'AdminCreated'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">Administrator added successfully! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.msg == 'AdminUpdated'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">Administrator updated successfully! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.msg == 'AdminDeleted'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">Administrator deleted successfully! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>
        <c:if test="${param.error == 'CannotDeleteSelf'}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">Error: You cannot delete your own account! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-4">ID</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Permission Level</th>
                                <th class="text-end pe-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="admin" items="${admins}">
                                <tr>
                                    <td class="ps-4 text-muted small">${admin.id}</td>
                                    <td class="fw-bold">
                                        <i class="fa-solid fa-user-shield text-danger me-2"></i>${admin.username}
                                        <c:if test="${admin.id == sessionScope.user.id}">
                                            <span class="badge bg-secondary ms-1">You</span>
                                        </c:if>
                                    </td>
                                    <td>${admin.email}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${admin.adminCode == 'SUPER_ADMIN'}">
                                                <span class="badge bg-danger"><i class="fa-solid fa-crown me-1"></i> SUPER_ADMIN</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-primary">${admin.adminCode}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end pe-4">
                                        <a href="${pageContext.request.contextPath}/admins?action=edit&id=${admin.id}" class="btn btn-sm btn-outline-primary me-2">
                                            <i class="fa-solid fa-edit"></i>
                                        </a>
                                        <c:if test="${admin.id != sessionScope.user.id}">
                                            <a href="${pageContext.request.contextPath}/admins?action=delete&id=${admin.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this Administrator?');">
                                                <i class="fa-solid fa-trash"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

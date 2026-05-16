<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN' || sessionScope.user.adminCode != 'SUPER_ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="pageTitle" value="Admin Security Dashboard - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0 text-danger"><i class="fa-solid fa-shield-halved me-2"></i>Security Dashboard</h2>
            <a href="${pageContext.request.contextPath}/admins?action=management" class="btn btn-outline-danger">
                <i class="fa-solid fa-users-cog me-2"></i>Manage Administrators
            </a>
        </div>

        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card bg-danger text-white shadow h-100 border-0">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <div class="text-uppercase fw-bold text-xs mb-1">Active Administrators</div>
                                <div class="h3 mb-0 fw-bold">${adminCount}</div>
                            </div>
                            <div class="col-auto">
                                <i class="fa-solid fa-user-shield fa-2x opacity-50"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-8 mb-4">
                <div class="card shadow-sm border-0 h-100">
                    <div class="card-header bg-dark text-white pt-3 pb-2">
                        <h6 class="fw-bold mb-0 font-monospace"><i class="fa-solid fa-terminal me-2"></i>System Activity Logs</h6>
                    </div>
                    <div class="card-body bg-light p-0" style="max-height: 400px; overflow-y: auto;">
                        <ul class="list-group list-group-flush font-monospace small">
                            <c:forEach var="log" items="${logs}">
                                <li class="list-group-item bg-transparent border-bottom border-light">
                                    <span class="text-success">></span> ${log}
                                </li>
                            </c:forEach>
                            <c:if test="${empty logs}">
                                <li class="list-group-item text-muted">No activity logs found.</li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

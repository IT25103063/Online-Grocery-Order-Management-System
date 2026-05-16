<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Session check (simple scriptlet logic or JSTL redirect) --%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="My Profile - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <h2 class="fw-bold mb-4">My Profile</h2>
        
        <div class="row">
            <div class="col-md-6">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <div class="text-center mb-4 mt-3">
                            <i class="fa-solid fa-user-circle fa-5x text-secondary mb-3"></i>
                            <h4 class="fw-bold">${sessionScope.user.username}</h4>
                            <span class="badge bg-success">${sessionScope.user.displayRole}</span>
                        </div>
                        
                        <hr>
                        
                        <div class="mb-3">
                            <label class="text-muted small fw-bold">Email Address</label>
                            <p class="mb-0 fs-5">${sessionScope.user.email}</p>
                        </div>
                        
                        <div class="mb-3">
                            <label class="text-muted small fw-bold">Internal ID</label>
                            <p class="mb-0 text-monospace">${sessionScope.user.id}</p>
                        </div>
                        
                        <div class="mt-4">
                            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger">
                                <i class="fa-solid fa-sign-out-alt me-2"></i>Log Out
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

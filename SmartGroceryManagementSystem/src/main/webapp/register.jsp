<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />

<div class="d-flex align-items-center justify-content-center min-vh-100 bg-light py-5">
    <div class="card shadow border-0" style="width: 450px; border-radius: 15px;">
        <div class="card-body p-5">
            <div class="text-center mb-4">
                <i class="fa-solid fa-user-plus text-success fa-3x mb-3"></i>
                <h3 class="fw-bold">Create an Account</h3>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email Address</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="mb-4">
                    <label for="role" class="form-label">Account Type</label>
                    <select class="form-select" id="role" name="role" required>
                        <option value="CUSTOMER">Customer (Instant Access)</option>
                        <option value="ADMIN">Administrator (Requires Approval)</option>
                    </select>
                    <small class="text-muted">Admin registration requires super admin approval</small>
                </div>
                <button type="submit" class="btn btn-success w-100 mb-3">Register</button>
            </form>

            <div class="text-center">
                <p class="mb-0">Already have an account? <a href="login.jsp" class="text-success fw-bold text-decoration-none">Login</a></p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/components/footer.jsp" />
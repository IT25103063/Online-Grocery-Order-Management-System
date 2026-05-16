<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />

<div class="d-flex align-items-center justify-content-center vh-100 bg-light">
    <div class="card shadow border-0" style="width: 400px; border-radius: 15px;">
        <div class="card-body p-5">
            <div class="text-center mb-4">
                <i class="fa-solid fa-basket-shopping text-success fa-3x mb-3"></i>
                <h3 class="fw-bold">Welcome Back</h3>
                <p class="text-muted">Sign in to your account</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                </div>
            </c:if>
            
            <c:if test="${param.registered == 'true'}">
                <div class="alert alert-success" role="alert">
                    <i class="fa-solid fa-check-circle me-2"></i>Registration successful. Please login.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="mb-4">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-success w-100 mb-3">Login</button>
            </form>
            
            <div class="text-center">
                <p class="mb-0">Don't have an account? <a href="register.jsp" class="text-success fw-bold text-decoration-none">Sign up</a></p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/components/footer.jsp" />

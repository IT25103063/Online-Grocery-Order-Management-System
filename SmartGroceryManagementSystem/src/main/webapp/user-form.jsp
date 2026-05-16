<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="isEdit" value="${not empty userObj}" />
<c:set var="pageTitle" value="${isEdit ? 'Edit User' : 'Add New User'} - Smart Grocery" scope="request" />

<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">
                <a href="${pageContext.request.contextPath}/users" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
                ${isEdit ? 'Edit User' : 'Add New User'}
            </h2>
        </div>

        <div class="row">
            <div class="col-lg-8">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4">
                        
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger" role="alert">
                                <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/users" method="post">
                            <input type="hidden" name="action" value="${isEdit ? 'update' : 'save'}">
                            <c:if test="${isEdit}">
                                <input type="hidden" name="id" value="${userObj.id}">
                            </c:if>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="username" class="form-label fw-bold">Username <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="username" name="username" value="${userObj.username}" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="email" class="form-label fw-bold">Email Address <span class="text-danger">*</span></label>
                                    <input type="email" class="form-control" id="email" name="email" value="${userObj.email}" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="password" class="form-label fw-bold">Password <c:if test="${!isEdit}"><span class="text-danger">*</span></c:if></label>
                                    <input type="password" class="form-control" id="password" name="password" ${!isEdit ? 'required' : ''} placeholder="${isEdit ? 'Leave blank to keep current' : ''}">
                                </div>
                                <div class="col-md-6">
                                    <label for="role" class="form-label fw-bold">Role <span class="text-danger">*</span></label>
                                    <c:choose>
                                        <c:when test="${isEdit}">
                                            <!-- Role cannot be changed once created in this basic version to avoid moving files -->
                                            <input type="text" class="form-control bg-light" value="${userObj.role}" readonly>
                                            <input type="hidden" name="role" value="${userObj.role}">
                                        </c:when>
                                        <c:otherwise>
                                            <select class="form-select" id="role" name="role" required>
                                                <option value="CUSTOMER">Customer</option>
                                                <option value="ADMIN">Administrator</option>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <c:if test="${isEdit}">
                                <div class="mb-4">
                                    <label for="specificField" class="form-label fw-bold">
                                        ${userObj.role == 'ADMIN' ? 'Admin Code' : 'Membership Level'}
                                    </label>
                                    <input type="text" class="form-control" id="specificField" name="specificField" value="${specificField}">
                                    <div class="form-text">This is a specific field demonstrating polymorphic updates.</div>
                                </div>
                            </c:if>

                            <hr class="my-4">
                            
                            <div class="d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/users" class="btn btn-light">Cancel</a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fa-solid fa-save me-2"></i>${isEdit ? 'Update User' : 'Save New User'}
                                </button>
                            </div>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

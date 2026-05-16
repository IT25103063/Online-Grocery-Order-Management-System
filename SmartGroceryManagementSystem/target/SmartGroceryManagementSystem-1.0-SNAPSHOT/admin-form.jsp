<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN' || sessionScope.user.adminCode != 'SUPER_ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="isEdit" value="${not empty adminObj}" />
<c:set var="pageTitle" value="${isEdit ? 'Edit Admin' : 'Add Admin'} - Smart Grocery" scope="request" />

<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <h2 class="fw-bold mb-4">
            <a href="${pageContext.request.contextPath}/admins?action=management" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
            ${isEdit ? 'Edit Administrator' : 'Add New Administrator'}
        </h2>

        <div class="row">
            <div class="col-lg-6">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/admins" method="post">
                            <input type="hidden" name="action" value="${isEdit ? 'update' : 'save'}">
                            <c:if test="${isEdit}">
                                <input type="hidden" name="id" value="${adminObj.id}">
                            </c:if>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Username <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="username" value="${adminObj.username}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Email Address <span class="text-danger">*</span></label>
                                <input type="email" class="form-control" name="email" value="${adminObj.email}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Password ${isEdit ? '<span class="text-muted fw-normal">(Leave blank to keep current)</span>' : '<span class="text-danger">*</span>'}</label>
                                <input type="password" class="form-control" name="password" ${isEdit ? '' : 'required'}>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Permission Level <span class="text-danger">*</span></label>
                                <select class="form-select" name="permission" required>
                                    <option value="MANAGER" ${adminObj.adminCode == 'MANAGER' ? 'selected' : ''}>Standard Manager (Manage Users/Products/Orders)</option>
                                    <option value="SUPER_ADMIN" ${adminObj.adminCode == 'SUPER_ADMIN' ? 'selected' : ''}>Super Admin (Full Access & Security Hub)</option>
                                </select>
                            </div>

                            <hr class="my-4">
                            
                            <div class="d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/admins?action=management" class="btn btn-light">Cancel</a>
                                <button type="submit" class="btn btn-danger">
                                    <i class="fa-solid fa-shield-check me-2"></i>${isEdit ? 'Update Administrator' : 'Create Administrator'}
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

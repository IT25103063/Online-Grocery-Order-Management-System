<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="isEdit" value="${not empty supplierObj}" />
<c:set var="pageTitle" value="${isEdit ? 'Edit Supplier' : 'Add Supplier'} - Smart Grocery" scope="request" />

<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <h2 class="fw-bold mb-4">
            <a href="${pageContext.request.contextPath}/suppliers" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
            ${isEdit ? 'Edit Supplier' : 'Add New Supplier'}
        </h2>

        <div class="row">
            <div class="col-lg-8">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4">
                        
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger" role="alert">
                                <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/suppliers" method="post">
                            <input type="hidden" name="action" value="${isEdit ? 'update' : 'save'}">
                            <c:if test="${isEdit}">
                                <input type="hidden" name="id" value="${supplierObj.id}">
                            </c:if>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Company Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="companyName" value="${supplierObj.companyName}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Contact Person</label>
                                    <input type="text" class="form-control" name="contactName" value="${supplierObj.contactName}">
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Email Address <span class="text-danger">*</span></label>
                                    <input type="email" class="form-control" name="email" value="${supplierObj.email}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Phone Number</label>
                                    <input type="text" class="form-control" name="phone" value="${supplierObj.phone}">
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Physical Address</label>
                                <textarea class="form-control" name="address" rows="3">${supplierObj.address}</textarea>
                            </div>

                            <hr class="my-4">
                            
                            <div class="d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/suppliers" class="btn btn-light">Cancel</a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa-solid fa-save me-2"></i>${isEdit ? 'Update Supplier' : 'Save Supplier'}
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

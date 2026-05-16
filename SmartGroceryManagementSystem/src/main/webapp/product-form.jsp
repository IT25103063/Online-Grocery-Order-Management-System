<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="isEdit" value="${not empty productObj}" />
<c:set var="pageTitle" value="${isEdit ? 'Edit Product' : 'Add New Product'} - Smart Grocery" scope="request" />

<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">
                <a href="${pageContext.request.contextPath}/products" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
                ${isEdit ? 'Edit Product' : 'Add New Product'}
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

                        <form action="${pageContext.request.contextPath}/products" method="post">
                            <input type="hidden" name="action" value="${isEdit ? 'update' : 'save'}">
                            <c:if test="${isEdit}">
                                <input type="hidden" name="id" value="${productObj.id}">
                            </c:if>

                            <div class="row mb-3">
                                <div class="col-md-12">
                                    <label class="form-label fw-bold">Product Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="name" value="${productObj.name}" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Price (Rs.) <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" min="0" class="form-control" name="price" value="${productObj.price}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Stock Quantity <span class="text-danger">*</span></label>
                                    <input type="number" min="0" class="form-control" name="stock" value="${productObj.stock}" required>
                                </div>
                            </div>

                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Category Type <span class="text-danger">*</span></label>
                                    <c:choose>
                                        <c:when test="${isEdit}">
                                            <input type="text" class="form-control bg-light" value="${productObj.type}" readonly>
                                            <input type="hidden" name="type" value="${productObj.type}">
                                        </c:when>
                                        <c:otherwise>
                                            <select class="form-select" id="typeSelect" name="type" required onchange="updateSpecialFieldLabel()">
                                                <option value="PERISHABLE">Perishable (Needs Expiration Date)</option>
                                                <option value="NON_PERISHABLE">Non-Perishable (Needs Warranty)</option>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="col-md-6">
                                    <label id="specialFieldLabel" class="form-label fw-bold">
                                        <c:choose>
                                            <c:when test="${isEdit && productObj.type == 'NON_PERISHABLE'}">Warranty Period (Months)</c:when>
                                            <c:otherwise>Expiration Date (YYYY-MM-DD)</c:otherwise>
                                        </c:choose>
                                        <span class="text-danger">*</span>
                                    </label>
                                    <c:choose>
                                        <c:when test="${isEdit && productObj.type == 'NON_PERISHABLE'}">
                                            <input type="number" min="0" class="form-control" name="specialField" value="${specialField}" required>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" class="form-control" id="specialFieldInput" name="specialField" value="${specialField}" placeholder="e.g. 2024-12-01" required>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <hr class="my-4">

                            <div class="d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-light">Cancel</a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fa-solid fa-save me-2"></i>${isEdit ? 'Update Product' : 'Save Product'}
                                </button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    function updateSpecialFieldLabel() {
        var type = document.getElementById("typeSelect").value;
        var label = document.getElementById("specialFieldLabel");
        var input = document.getElementById("specialFieldInput");

        if (type === "PERISHABLE") {
            label.innerHTML = 'Expiration Date (YYYY-MM-DD) <span class="text-danger">*</span>';
            if (input) {
                input.type = "text";
                input.placeholder = "e.g. 2024-12-01";
            }
        } else {
            label.innerHTML = 'Warranty Period (Months) <span class="text-danger">*</span>';
            if (input) {
                input.type = "number";
                input.placeholder = "e.g. 12";
                input.min = "0";
            }
        }
    }
</script>

<jsp:include page="/WEB-INF/components/footer.jsp" />
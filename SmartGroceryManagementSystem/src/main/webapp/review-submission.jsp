<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="Write a Review - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <h2 class="fw-bold mb-4">
            <a href="${pageContext.request.contextPath}/products" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
            Write a Review
        </h2>

        <div class="row">
            <div class="col-lg-6">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center mb-4 pb-3 border-bottom">
                            <i class="fa-solid fa-box fa-3x text-muted me-3"></i>
                            <div>
                                <h5 class="fw-bold mb-0">${productObj.name}</h5>
                                <small class="text-muted">Product ID: ${productObj.id}</small>
                            </div>
                        </div>

                        <c:if test="${not empty param.error}">
                            <div class="alert alert-danger" role="alert">
                                <i class="fa-solid fa-circle-exclamation me-2"></i>Failed to submit review. Please try again.
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/reviews" method="post">
                            <input type="hidden" name="action" value="save">
                            <input type="hidden" name="productId" value="${productObj.id}">

                            <div class="mb-4">
                                <label class="form-label fw-bold">Your Rating <span class="text-danger">*</span></label>
                                <select class="form-select" name="rating" required>
                                    <option value="5">★★★★★ - Excellent</option>
                                    <option value="4">★★★★☆ - Very Good</option>
                                    <option value="3">★★★☆☆ - Average</option>
                                    <option value="2">★★☆☆☆ - Poor</option>
                                    <option value="1">★☆☆☆☆ - Terrible</option>
                                </select>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Review Comment <span class="text-danger">*</span></label>
                                <textarea class="form-control" name="comment" rows="4" placeholder="What did you like or dislike about this product?" required></textarea>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Order ID <span class="text-muted fw-normal">(Optional - For Verified Purchase Badge)</span></label>
                                <input type="text" class="form-control" name="orderId" placeholder="e.g. ORD-XYZ123">
                            </div>

                            <hr class="my-4">
                            
                            <div class="d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-light">Cancel</a>
                                <button type="submit" class="btn btn-warning text-dark fw-bold">
                                    <i class="fa-solid fa-paper-plane me-2"></i>Submit Review
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

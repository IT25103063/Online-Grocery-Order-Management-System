<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Product Reviews - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<main class="main-content">
    <div class="container-fluid">
        
        <c:if test="${param.msg == 'ReviewAdded'}">
            <div class="alert alert-success alert-dismissible fade show"><i class="fa-solid fa-check-circle me-2"></i>Thank you! Your review was published. <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>

        <div class="row mb-4 align-items-center">
            <div class="col-md-8">
                <h2 class="fw-bold mb-1">
                    <a href="${pageContext.request.contextPath}/products" class="text-muted text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
                    Reviews for ${productObj.name}
                </h2>
                <div class="d-flex align-items-center text-warning fs-5">
                    <i class="fa-solid fa-star me-2"></i> 
                    <span class="text-dark fw-bold me-2">${avgRating}</span> 
                    <span class="text-muted fs-6">(${reviews.size()} total ratings)</span>
                </div>
            </div>
            <div class="col-md-4 text-end">
                <a href="${pageContext.request.contextPath}/reviews?action=new&productId=${productObj.id}" class="btn btn-warning text-dark fw-bold">
                    <i class="fa-solid fa-pen-nib me-2"></i>Write a Review
                </a>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-8">
                <c:forEach var="review" items="${reviews}">
                    <div class="card shadow-sm border-0 mb-3">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <div>
                                    <span class="fw-bold me-2"><i class="fa-solid fa-user-circle text-muted me-1"></i>${review.userId}</span>
                                    <small class="text-muted">${review.date}</small>
                                </div>
                                <div>
                                    ${review.displayBadge}
                                </div>
                            </div>
                            
                            <div class="text-warning mb-2">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fa-${i <= review.rating ? 'solid' : 'regular'} fa-star"></i>
                                </c:forEach>
                            </div>
                            
                            <p class="mb-0 text-dark">${review.comment}</p>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty reviews}">
                    <div class="text-center py-5">
                        <i class="fa-regular fa-comments fa-4x text-muted mb-3"></i>
                        <h4 class="text-muted">No reviews yet!</h4>
                        <p class="text-muted">Be the first to review this product.</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

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
    <h2 class="fw-bold mb-4">Write a Review</h2>

    <c:if test="${not empty param.msg}">
      <div class="alert alert-success alert-dismissible fade show">
        <i class="fa-solid fa-check-circle me-2"></i>${param.msg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>
    <c:if test="${not empty param.error}">
      <div class="alert alert-danger alert-dismissible fade show">
        <i class="fa-solid fa-circle-exclamation me-2"></i>${param.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <div class="row">
      <div class="col-md-6">
        <div class="card shadow-sm border-0">
          <div class="card-header bg-white pt-3 pb-2">
            <h5 class="fw-bold mb-0">Select a Product to Review</h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                <tr>
                  <th>Product Name</th>
                  <th>Price</th>
                  <th class="text-center">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="prod" items="${products}">
                  <tr>
                    <td>
                      <div class="fw-bold">${prod.name}</div>
                      <div class="small text-muted">${prod.specialDetail}</div>
                    </td>
                    <td class="fw-bold text-success">Rs. ${prod.price}</td>
                    <td class="text-center">
                      <a href="${pageContext.request.contextPath}/reviews?action=new&productId=${prod.id}"
                         class="btn btn-sm btn-outline-warning">
                        <i class="fa-solid fa-star me-1"></i> Write Review
                      </a>
                    </td>
                  </tr>
                </c:forEach>
                <c:if test="${empty products}">
                  <tr>
                    <td colspan="3" class="text-center py-4 text-muted">
                      No products available to review.
                    </td>
                  </tr>
                </c:if>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card shadow-sm border-0">
          <div class="card-header bg-white pt-3 pb-2">
            <h5 class="fw-bold mb-0">Your Recent Reviews</h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                <tr>
                  <th>Product ID</th>
                  <th>Rating</th>
                  <th>Date</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="review" items="${userReviews}">
                  <tr>
                    <td class="fw-bold">${review.productId}</td>
                    <td class="text-warning">${review.rating} ★</td>
                    <td class="text-muted small">${review.date}</td>
                  </tr>
                </c:forEach>
                <c:if test="${empty userReviews}">
                  <tr>
                    <td colspan="3" class="text-center py-4 text-muted">
                      You haven't written any reviews yet.
                    </td>
                  </tr>
                </c:if>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />
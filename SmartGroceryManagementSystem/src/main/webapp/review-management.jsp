<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN'}">
    <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="pageTitle" value="Review Moderation - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
    <div class="container-fluid">
        <h2 class="fw-bold mb-4">Review Moderation</h2>

        <c:if test="${param.msg == 'ReviewDeleted'}">
            <div class="alert alert-warning alert-dismissible fade show"><i class="fa-solid fa-trash me-2"></i>Review permanently deleted.<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
        </c:if>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-4">Review ID</th>
                                <th>Product ID</th>
                                <th>User</th>
                                <th>Rating</th>
                                <th>Comment Snippet</th>
                                <th>Type</th>
                                <th class="text-end pe-4">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${allReviews}">
                                <tr>
                                    <td class="ps-4 text-muted small">${r.id}</td>
                                    <td class="fw-bold">${r.productId}</td>
                                    <td>${r.userId}</td>
                                    <td class="text-warning fw-bold">${r.rating} ★</td>
                                    <td class="text-truncate" style="max-width: 200px;">${r.comment}</td>
                                    <td>${r.displayBadge}</td>
                                    <td class="text-end pe-4">
                                        <a href="${pageContext.request.contextPath}/reviews?action=delete&id=${r.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Permanently delete this review?');">
                                            <i class="fa-solid fa-trash me-1"></i>Delete
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allReviews}">
                                <tr>
                                    <td colspan="7" class="text-center py-4 text-muted">No reviews in the system.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

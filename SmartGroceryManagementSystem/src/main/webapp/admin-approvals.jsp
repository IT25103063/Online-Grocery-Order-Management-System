<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'ADMIN'}">
  <c:redirect url="dashboard.jsp" />
</c:if>

<c:set var="pageTitle" value="Admin Approvals - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
  <div class="container-fluid">
    <h2 class="fw-bold mb-4">
      <i class="fa-solid fa-user-check text-success me-2"></i>
      Admin Approval Requests
    </h2>

    <c:if test="${param.msg == 'Approved'}">
      <div class="alert alert-success">Admin approved successfully!</div>
    </c:if>

    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead class="table-light">
            <tr>
              <th>Username</th>
              <th>Email</th>
              <th>Request Date</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="pending" items="${pendingList}">
              <tr>
                <td class="fw-bold">${pending.username}</td>
                <td>${pending.email}</td>
                <td>${pending.requestDate}</td>
                <td>
                  <span class="badge bg-warning text-dark">Pending</span>
                </td>
                <td>
                  <a href="${pageContext.request.contextPath}/admin-approvals?action=approve&id=${pending.id}"
                     class="btn btn-sm btn-success"
                     onclick="return confirm('Approve this admin request?');">
                    <i class="fa-solid fa-check me-1"></i> Approve
                  </a>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty pendingList}">
              <tr>
                <td colspan="5" class="text-center py-4 text-muted">
                  No pending admin requests.
                </td>
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
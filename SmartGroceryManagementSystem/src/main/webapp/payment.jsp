<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${empty sessionScope.user}">
  <c:redirect url="login.jsp" />
</c:if>

<c:set var="pageTitle" value="Card Payment - Smart Grocery" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<main class="main-content">
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow-sm border-0">
          <div class="card-header bg-white text-center pt-4">
            <i class="fa-solid fa-credit-card fa-3x text-primary mb-2"></i>
            <h4 class="fw-bold">Card Payment</h4>
            <p class="text-muted">Enter your card details to complete payment</p>
          </div>
          <div class="card-body p-4">

            <c:if test="${not empty param.error}">
              <div class="alert alert-danger">
                <i class="fa-solid fa-circle-exclamation me-2"></i>${param.error}
              </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/payment" method="post" id="paymentForm">
              <div class="mb-3">
                <label class="form-label fw-bold">Card Number</label>
                <div class="input-group">
                  <span class="input-group-text bg-white"><i class="fa-solid fa-credit-card"></i></span>
                  <input type="text" class="form-control" id="cardNumber" name="cardNumber"
                         placeholder="1234 5678 9012 3456" maxlength="19" required>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-bold">Expiry Date</label>
                  <input type="text" class="form-control" name="expiry"
                         placeholder="MM/YY" maxlength="5" required>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-bold">CVV</label>
                  <input type="password" class="form-control" name="cvv"
                         placeholder="123" maxlength="3" required>
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label fw-bold">Cardholder Name</label>
                <input type="text" class="form-control" name="cardName"
                       placeholder="Name on card" required>
              </div>

              <hr>

              <div class="mb-3">
                <div class="d-flex justify-content-between">
                  <span class="fw-bold">Total Amount:</span>
                  <span class="fw-bold fs-5 text-success">${param.amount}</span>
                </div>
              </div>

              <input type="hidden" name="orderId" value="${param.orderId}">
              <input type="hidden" name="amount" value="${param.amount}">

              <button type="submit" class="btn btn-success w-100 py-2 fw-bold">
                <i class="fa-solid fa-lock me-2"></i>Pay Now
              </button>
            </form>

            <div class="text-center mt-3">
              <small class="text-muted">
                <i class="fa-solid fa-shield-alt me-1"></i> Test Card: 4242 4242 4242 4242 | Any CVV | Any Expiry
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<script>
  document.getElementById('cardNumber').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\s/g, '');
    if (value.length > 16) value = value.slice(0, 16);
    value = value.replace(/(\d{4})/g, '$1 ').trim();
    e.target.value = value;
  });
</script>

<jsp:include page="/WEB-INF/components/footer.jsp" />
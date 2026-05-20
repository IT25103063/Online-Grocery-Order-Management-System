<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Forgot Password – Smart Grocery</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
<div class="auth-wrapper">

  <div class="auth-left">
    <div class="auth-left-inner">
      <div class="brand-logo">🛒</div>
      <h1 class="brand-name">Smart Grocery</h1>
      <p class="brand-tagline">Secure password reset</p>
      <div class="brand-features">
        <div class="feature-item">📧 OTP sent to your email</div>
        <div class="feature-item">⏱ Valid for 5 minutes</div>
        <div class="feature-item">🔒 Safe &amp; secure</div>
      </div>
    </div>
    <div class="auth-left-footer">© 2025 Smart Grocery.</div>
  </div>

  <div class="auth-right">
    <div class="auth-form-box">

      <c:if test="${not empty error}">
        <div class="error-msg-box">❌ ${error}</div>
      </c:if>

      <%-- STEP 1: Enter email --%>
      <c:if test="${empty step || step == 'find'}">
        <div class="auth-top">
          <h2>🔑 Forgot Password</h2>
          <p>Enter your registered email address.</p>
        </div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
          <input type="hidden" name="action" value="find">
          <div class="form-group">
            <label>📧 Email Address</label>
            <div class="input-wrap">
              <span class="input-icon">📧</span>
              <input type="email" name="email" placeholder="Enter your email" required>
            </div>
          </div>
          <button type="submit" class="btn-primary-auth">📨 Send OTP</button>
        </form>
      </c:if>

      <%-- STEP 2: Enter OTP --%>
      <c:if test="${step == 'otp'}">
        <div class="auth-top">
          <h2>📩 Enter OTP</h2>
          <p>We sent a 6-digit OTP to <strong>${email}</strong>. Check your inbox.</p>
        </div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
          <input type="hidden" name="action" value="verify">
          <div class="form-group">
            <label>🔢 OTP Code</label>
            <div class="input-wrap">
              <span class="input-icon">🔢</span>
              <input type="text" name="otp" placeholder="Enter 6-digit OTP" maxlength="6" required style="letter-spacing:6px;font-size:1.4rem;font-weight:700;">
            </div>
          </div>
          <button type="submit" class="btn-primary-auth">✅ Verify OTP</button>
        </form>
      </c:if>

      <%-- STEP 3: Reset password --%>
      <c:if test="${step == 'reset'}">
        <div class="auth-top">
          <h2>🔒 Set New Password</h2>
          <p>OTP verified! Enter your new password.</p>
        </div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
          <input type="hidden" name="action" value="reset">
          <div class="form-group">
            <label>🔒 New Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" id="np" name="newPassword" placeholder="Min 6 characters" required>
              <span class="input-eye" onclick="toggle('np')">👁</span>
            </div>
          </div>
          <div class="form-group">
            <label>🔒 Confirm Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" id="cp" name="confirmPassword" placeholder="Repeat password" required>
              <span class="input-eye" onclick="toggle('cp')">👁</span>
            </div>
          </div>
          <button type="submit" class="btn-primary-auth">✅ Reset Password</button>
        </form>
      </c:if>

      <div class="auth-divider"><span>or</span></div>
      <p class="auth-switch">
        Remember it? <a href="${pageContext.request.contextPath}/login.jsp">🔐 Sign In</a>
      </p>
    </div>
  </div>
</div>
<script>
function toggle(id) {
  const i = document.getElementById(id);
  i.type = i.type === 'password' ? 'text' : 'password';
}
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Forgot Password – Smart Grocery</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body.auth-body {
      background: linear-gradient(135deg, #f8fafc 0%, #eef2f5 100%);
      font-family: 'Inter', system-ui, -apple-system, sans-serif;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem 1.5rem;
    }

    .auth-wrapper {
      max-width: 1200px;
      width: 100%;
      margin: 0 auto;
      display: flex;
      flex-wrap: wrap;
      background: #ffffff;
      border-radius: 2rem;
      box-shadow: 0 25px 45px -12px rgba(0, 0, 0, 0.2);
      overflow: hidden;
    }

    /* LEFT PANEL - Branding (Fixed Alignment) */
    .auth-left {
      flex: 1.2;
      background: #1e3a2f;
      padding: 2.5rem 2rem;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      color: white;
    }

    .auth-left-inner {
      flex: 1;
    }

    .brand-logo {
      font-size: 3rem;
      margin-bottom: 1rem;
    }

    .brand-name {
      font-size: 2rem;
      font-weight: 700;
      margin-bottom: 0.5rem;
      letter-spacing: -0.5px;
    }

    .brand-tagline {
      font-size: 0.95rem;
      opacity: 0.85;
      margin-bottom: 2rem;
      border-left: 3px solid #7ac68f;
      padding-left: 0.8rem;
    }

    .brand-features {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      margin-top: 1.5rem;
    }

    .feature-item {
      display: flex;
      align-items: center;
      gap: 0.8rem;
      font-size: 0.95rem;
      background: rgba(255,255,255,0.08);
      padding: 0.7rem 1rem;
      border-radius: 2rem;
    }

    .auth-left-footer {
      font-size: 0.7rem;
      opacity: 0.7;
      margin-top: 2rem;
      padding-top: 1rem;
      border-top: 1px solid rgba(255,255,255,0.2);
    }

    /* RIGHT PANEL - Form (Fixed Alignment) */
    .auth-right {
      flex: 1;
      background: #ffffff;
      padding: 2.5rem 2.2rem;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }

    .auth-form-box {
      width: 100%;
    }

    .auth-top h2 {
      font-size: 1.8rem;
      font-weight: 700;
      color: #0f2c22;
      margin-bottom: 0.5rem;
    }

    .auth-top p {
      font-size: 0.9rem;
      color: #5a6e64;
      margin-bottom: 1.5rem;
    }

    /* Form Groups - Perfect Alignment */
    .form-group {
      margin-bottom: 1.5rem;
    }

    .form-group label {
      font-size: 0.85rem;
      font-weight: 600;
      color: #2c4b3c;
      margin-bottom: 0.5rem;
      display: block;
    }

    .input-wrap {
      position: relative;
      display: flex;
      align-items: center;
    }

    .input-icon {
      position: absolute;
      left: 1rem;
      font-size: 1rem;
      z-index: 1;
    }

    .input-wrap input {
      width: 100%;
      padding: 0.85rem 1rem 0.85rem 2.5rem;
      font-size: 0.95rem;
      border: 1.5px solid #e2e8f0;
      border-radius: 1rem;
      transition: all 0.2s;
      outline: none;
      font-family: inherit;
    }

    .input-wrap input:focus {
      border-color: #2f855a;
      box-shadow: 0 0 0 3px rgba(47, 133, 90, 0.1);
    }

    .input-eye {
      position: absolute;
      right: 1rem;
      cursor: pointer;
      opacity: 0.6;
      z-index: 1;
    }

    .input-eye:hover {
      opacity: 1;
    }

    /* OTP specific styling */
    .input-wrap input[name="otp"] {
      letter-spacing: 6px;
      font-size: 1.4rem;
      font-weight: 700;
      text-align: center;
      padding: 0.85rem 1rem;
    }

    /* Button Styles */
    .btn-primary-auth {
      width: 100%;
      background: #1e3a2f;
      color: white;
      border: none;
      padding: 0.9rem;
      font-size: 1rem;
      font-weight: 600;
      border-radius: 2rem;
      cursor: pointer;
      transition: all 0.2s;
      margin-bottom: 1.2rem;
    }

    .btn-primary-auth:hover {
      background: #123126;
      transform: translateY(-2px);
      box-shadow: 0 6px 14px rgba(30, 58, 47, 0.25);
    }

    /* Messages */
    .success-msg, .error-msg-box {
      padding: 0.75rem 1rem;
      border-radius: 1rem;
      margin-bottom: 1.5rem;
      font-size: 0.85rem;
    }

    .success-msg {
      background: #d1fae5;
      color: #065f46;
      border-left: 4px solid #10b981;
    }

    .error-msg-box {
      background: #fee2e2;
      color: #991b1b;
      border-left: 4px solid #ef4444;
    }

    .auth-divider {
      text-align: center;
      margin: 1rem 0;
      position: relative;
    }

    .auth-divider::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      width: 45%;
      height: 1px;
      background: #e2e8f0;
    }

    .auth-divider::after {
      content: '';
      position: absolute;
      right: 0;
      top: 50%;
      width: 45%;
      height: 1px;
      background: #e2e8f0;
    }

    .auth-divider span {
      background: white;
      padding: 0 0.8rem;
      font-size: 0.8rem;
      color: #9ca3af;
    }

    .auth-switch {
      text-align: center;
      font-size: 0.9rem;
      color: #3c5c4b;
    }

    .auth-switch a {
      color: #1e6b48;
      font-weight: 700;
      text-decoration: none;
    }

    .auth-switch a:hover {
      text-decoration: underline;
    }

    /* Responsive */
    @media (max-width: 780px) {
      .auth-wrapper {
        flex-direction: column;
        border-radius: 1.5rem;
      }
      .auth-left {
        padding: 1.8rem;
      }
      .auth-right {
        padding: 2rem 1.5rem;
      }
      .auth-top h2 {
        font-size: 1.5rem;
      }
    }
  </style>
</head>
<body class="auth-body">
<div class="auth-wrapper">

  <!-- Left Panel - Branding (Fixed Alignment) -->
  <div class="auth-left">
    <div class="auth-left-inner">
      <div class="brand-logo">🛒</div>
      <h1 class="brand-name">Smart Grocery</h1>
      <p class="brand-tagline">Secure password reset</p>
      <div class="brand-features">
        <div class="feature-item">
          <i class="fas fa-envelope"></i>
          <span>📧 OTP sent to your email</span>
        </div>
        <div class="feature-item">
          <i class="fas fa-clock"></i>
          <span>⏱ Valid for 5 minutes</span>
        </div>
        <div class="feature-item">
          <i class="fas fa-lock"></i>
          <span>🔒 Safe &amp; secure</span>
        </div>
      </div>
    </div>
    <div class="auth-left-footer">© 2025 Smart Grocery. All rights reserved.</div>
  </div>

  <!-- Right Panel - Form (Fixed Alignment) -->
  <div class="auth-right">
    <div class="auth-form-box">

      <c:if test="${not empty error}">
        <div class="error-msg-box">
          <i class="fas fa-exclamation-circle"></i> ${error}
        </div>
      </c:if>

      <%-- STEP 1: Enter email --%>
      <c:if test="${empty step || step == 'find'}">
        <div class="auth-top">
          <h2><i class="fas fa-key"></i> Forgot Password</h2>
          <p>Enter your registered email address to receive a verification code.</p>
        </div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
          <input type="hidden" name="action" value="find">
          <div class="form-group">
            <label><i class="fas fa-envelope"></i> Email Address</label>
            <div class="input-wrap">
              <span class="input-icon">📧</span>
              <input type="email" name="email" placeholder="Enter your registered email" required>
            </div>
          </div>
          <button type="submit" class="btn-primary-auth">
            <i class="fas fa-paper-plane"></i> Send OTP
          </button>
        </form>
      </c:if>

      <%-- STEP 2: Enter OTP --%>
      <c:if test="${step == 'otp'}">
        <div class="auth-top">
          <h2><i class="fas fa-envelope-open-text"></i> Enter OTP</h2>
          <p>We've sent a verification code to <strong>${email}</strong></p>
          <c:if test="${not empty otpHint}">
            <div class="success-msg" style="font-size: 1.2rem; text-align: center;">
              <i class="fas fa-keyboard"></i> Your OTP: <strong style="font-size: 1.5rem; letter-spacing: 4px;">${otpHint}</strong>
            </div>
          </c:if>
        </div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
          <input type="hidden" name="action" value="verify">
          <div class="form-group">
            <label><i class="fas fa-hashtag"></i> OTP Code</label>
            <div class="input-wrap">
              <span class="input-icon">🔢</span>
              <input type="text" name="otp" placeholder="000000" maxlength="6" required
                     style="letter-spacing: 6px; font-size: 1.4rem; font-weight: 700; text-align: center;">
            </div>
            <small class="text-muted">Enter the 6-digit code sent to your email</small>
          </div>
          <button type="submit" class="btn-primary-auth">
            <i class="fas fa-check-circle"></i> Verify OTP
          </button>
        </form>
      </c:if>

      <%-- STEP 3: Reset password --%>
      <c:if test="${step == 'reset'}">
        <div class="auth-top">
          <h2><i class="fas fa-lock"></i> Set New Password</h2>
          <p>OTP verified! Create a strong new password for your account.</p>
        </div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post" onsubmit="return validatePassword()">
          <input type="hidden" name="action" value="reset">
          <div class="form-group">
            <label><i class="fas fa-key"></i> New Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" id="np" name="newPassword" placeholder="Min 6 characters" required>
              <span class="input-eye" onclick="togglePassword('np')">
                                <i class="fas fa-eye"></i>
                            </span>
            </div>
          </div>
          <div class="form-group">
            <label><i class="fas fa-key"></i> Confirm Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" id="cp" name="confirmPassword" placeholder="Repeat your new password" required>
              <span class="input-eye" onclick="togglePassword('cp')">
                                <i class="fas fa-eye"></i>
                            </span>
            </div>
          </div>
          <button type="submit" class="btn-primary-auth">
            <i class="fas fa-save"></i> Reset Password
          </button>
        </form>
      </c:if>

      <div class="auth-divider"><span>or</span></div>
      <p class="auth-switch">
        <i class="fas fa-arrow-left"></i> Remember it?
        <a href="${pageContext.request.contextPath}/login.jsp"> Sign In</a>
      </p>
    </div>
  </div>
</div>

<script>
  function togglePassword(fieldId) {
    const field = document.getElementById(fieldId);
    const eyeIcon = field.nextElementSibling.querySelector('i');

    if (field.type === 'password') {
      field.type = 'text';
      if (eyeIcon) eyeIcon.classList.remove('fa-eye');
      if (eyeIcon) eyeIcon.classList.add('fa-eye-slash');
    } else {
      field.type = 'password';
      if (eyeIcon) eyeIcon.classList.remove('fa-eye-slash');
      if (eyeIcon) eyeIcon.classList.add('fa-eye');
    }
  }

  function validatePassword() {
    const newPass = document.getElementById('np').value;
    const confirmPass = document.getElementById('cp').value;

    if (newPass !== confirmPass) {
      alert('❌ Passwords do not match! Please try again.');
      return false;
    }

    if (newPass.length < 6) {
      alert('❌ Password must be at least 6 characters long!');
      return false;
    }

    return true;
  }

  // Auto-focus OTP input if on OTP step
  <c:if test="${step == 'otp'}">
  document.addEventListener('DOMContentLoaded', function() {
    const otpInput = document.querySelector('input[name="otp"]');
    if (otpInput) otpInput.focus();
  });
  </c:if>
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
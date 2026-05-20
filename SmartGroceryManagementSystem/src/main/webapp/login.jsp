<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login – Smart Grocery</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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

    /* RIGHT PANEL - Login Form (Fixed Alignment) */
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

    /* Role Tabs - Fixed Alignment */
    .role-tabs {
      display: flex;
      gap: 1rem;
      margin-bottom: 2rem;
      background: #f1f5f9;
      padding: 0.5rem;
      border-radius: 1rem;
    }

    .role-tab {
      flex: 1;
      padding: 0.7rem;
      text-align: center;
      cursor: pointer;
      font-weight: 600;
      font-size: 0.9rem;
      border: none;
      background: transparent;
      border-radius: 0.75rem;
      transition: all 0.2s;
      color: #6b7280;
    }

    .role-tab.active-customer {
      background: #1e3a2f;
      color: white;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .role-tab.active-admin {
      background: #dc2626;
      color: white;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
    }

    /* Form Options - Flex Alignment Fixed */
    .form-options {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 1rem 0 1.5rem 0;
      flex-wrap: wrap;
      gap: 0.8rem;
    }

    .remember-me {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 0.85rem;
      color: #2f4d3e;
      cursor: pointer;
    }

    .remember-me input {
      width: 1rem;
      height: 1rem;
      accent-color: #2f855a;
      margin: 0;
      cursor: pointer;
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
      <p class="brand-tagline">Your daily fresh grocery destination</p>
      <div class="brand-features">
        <div class="feature-item">✅ Fresh produce daily</div>
        <div class="feature-item">🚚 Fast doorstep delivery</div>
        <div class="feature-item">🎂 Birthday special discounts</div>
        <div class="feature-item">🔒 Safe &amp; secure checkout</div>
      </div>
    </div>
    <div class="auth-left-footer">© 2025 Smart Grocery. All rights reserved.</div>
  </div>

  <!-- Right Panel - Login Form (Fixed Alignment) -->
  <div class="auth-right">
    <div class="auth-form-box">
      <div class="auth-top">
        <h2 id="loginTitle">Sign in 👋</h2>
        <p id="loginSubtitle">Welcome back! Choose your account type.</p>
      </div>

      <!-- Role Tabs -->
      <div class="role-tabs">
        <button class="role-tab active-customer" id="tabCustomer" onclick="switchTab('customer')">🛒 Customer</button>
        <button class="role-tab" id="tabAdmin" onclick="switchTab('admin')">🛡️ Admin</button>
      </div>

      <!-- Messages -->
      <c:if test="${param.registered == 'true'}">
        <div class="success-msg">✅ Registered successfully! Please sign in.</div>
      </c:if>
      <c:if test="${param.msg == 'PasswordReset'}">
        <div class="success-msg">✅ Password reset! Please sign in.</div>
      </c:if>
      <c:if test="${not empty errorMessage}">
        <div class="error-msg-box">❌ ${errorMessage}</div>
      </c:if>

      <!-- Login Form -->
      <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="hidden" name="loginType" id="loginType" value="customer">

        <div class="form-group">
          <label>👤 Username</label>
          <div class="input-wrap">
            <span class="input-icon">👤</span>
            <input type="text" name="username" placeholder="Enter your username" required>
          </div>
        </div>

        <div class="form-group">
          <label>🔒 Password</label>
          <div class="input-wrap">
            <span class="input-icon">🔒</span>
            <input type="password" id="pw" name="password" placeholder="••••••••" required>
            <span class="input-eye" onclick="togglePass()">👁</span>
          </div>
        </div>

        <div class="form-options">
          <label class="remember-me">
            <input type="checkbox" name="remember"> Remember me
          </label>
          <a href="${pageContext.request.contextPath}/forgot-password" class="text-success small fw-semibold">🔑 Forgot Password?</a>
        </div>

        <button type="submit" class="btn-primary-auth" id="submitBtn">🚀 Sign In as Customer</button>
      </form>

      <div id="registerLink">
        <div class="auth-divider"><span>or</span></div>
        <p class="auth-switch">Don't have an account? <a href="${pageContext.request.contextPath}/register">📝 Create one free</a></p>
      </div>
    </div>
  </div>
</div>

<script>
  function switchTab(type) {
    const isAdmin = type === 'admin';
    document.getElementById('loginType').value = type;

    // Update tab styles
    document.getElementById('tabCustomer').className = 'role-tab' + (!isAdmin ? ' active-customer' : '');
    document.getElementById('tabAdmin').className = 'role-tab' + (isAdmin ? ' active-admin' : '');

    // Update text content
    document.getElementById('loginTitle').textContent = isAdmin ? 'Admin Login 🛡️' : 'Sign in 👋';
    document.getElementById('loginSubtitle').textContent = isAdmin ? 'Welcome, Administrator!' : 'Welcome back! Choose your account type.';
    document.getElementById('submitBtn').innerHTML = isAdmin ? '🛡️ Sign In as Admin' : '🚀 Sign In as Customer';

    // Hide register link for admin login
    document.getElementById('registerLink').style.display = isAdmin ? 'none' : 'block';
  }

  function togglePass() {
    const pwInput = document.getElementById('pw');
    pwInput.type = pwInput.type === 'password' ? 'text' : 'password';
  }

  // Preserve tab state after error
  <c:if test="${not empty errorMessage and param.loginType == 'admin'}">
  switchTab('admin');
  </c:if>
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
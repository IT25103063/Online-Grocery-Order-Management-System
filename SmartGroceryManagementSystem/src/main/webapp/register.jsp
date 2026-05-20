<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>📝 Register – Smart Grocery</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">

<div class="auth-wrapper">

  <div class="auth-left">
    <div class="auth-left-inner">
      <div class="brand-logo">🛒</div>
      <h1 class="brand-name">Smart Grocery</h1>
      <p class="brand-tagline">Join thousands of happy shoppers</p>
      <div class="brand-features">
        <div class="feature-item">🎁 Welcome discount on first order</div>
        <div class="feature-item">🎂 Birthday 50% off your total bill</div>
        <div class="feature-item">📦 Track your orders live</div>
        <div class="feature-item">🌿 100% fresh guaranteed</div>
      </div>
    </div>
    <div class="auth-left-footer">© 2025 Smart Grocery. All rights reserved.</div>
  </div>

  <div class="auth-right">
    <div class="auth-form-box register-box">

      <div class="auth-top">
        <h2>Create Account 🌿</h2>
        <p>Fill in your details to get started for free.</p>
      </div>

      <!-- Progress steps — your exact design -->
      <div class="progress-steps">
        <div class="step active" id="s1"><div class="step-circle">1</div><span>Personal</span></div>
        <div class="step-line"></div>
        <div class="step" id="s2"><div class="step-circle">2</div><span>Account</span></div>
        <div class="step-line"></div>
        <div class="step" id="s3"><div class="step-circle">3</div><span>Done</span></div>
      </div>

      <c:if test="${not empty errorMessage}">
        <div class="error-msg-box">❌ ${errorMessage}</div>
      </c:if>
      <div class="error-msg-box" id="regErr" style="display:none;"></div>

      <!-- form action keeps original backend URL -->
      <form action="${pageContext.request.contextPath}/register" method="post" id="regForm">

        <!-- STEP 1 -->
        <div id="step1">
          <div class="form-row">
            <div class="form-group">
              <label>👤 Full Name</label>
              <div class="input-wrap">
                <span class="input-icon">👤</span>
                <input type="text" id="r-name" name="username" placeholder="John Doe">
              </div>
            </div>
            <div class="form-group">
              <label>📧 Email</label>
              <div class="input-wrap">
                <span class="input-icon">📧</span>
                <input type="email" id="r-email" name="email" placeholder="you@example.com">
              </div>
            </div>
          </div>
          <button type="button" class="btn-primary-auth" onclick="goStep(2)">Continue →</button>
        </div>

        <!-- STEP 2 -->
        <div id="step2" style="display:none;">
          <div class="form-group">
            <label>🔒 Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" id="r-pass" name="password" placeholder="Min. 6 characters">
              <span class="input-eye" onclick="toggleReg()">👁</span>
            </div>
          </div>
          <div class="form-group">
            <label>🔒 Confirm Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" id="r-conf" placeholder="Repeat password">
            </div>
          </div>
          <div class="form-group">
            <label>🎭 Account Type</label>
            <select name="role" class="form-select" style="border-radius:10px; border:1.5px solid #e5e5e5; padding:0.65rem 1rem;">
              <option value="CUSTOMER">🛍️ Customer</option>
              <option value="ADMIN">⚙️ Administrator</option>
            </select>
          </div>
          <div class="step-btns">
            <button type="button" class="btn-secondary-auth" onclick="goStep(1)">← Back</button>
            <button type="button" class="btn-primary-auth" onclick="goStep(3)">Continue →</button>
          </div>
        </div>

        <!-- STEP 3 -->
        <div id="step3" style="display:none;">
          <div class="birthday-info-box">
            <div class="bday-icon">🎂</div>
            <div>
              <h4>Birthday Benefit!</h4>
              <p>Register to get <strong>50% off</strong> on your birthday if order is above <strong>Rs.1,000</strong>!</p>
            </div>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" id="terms">
              I agree to the <a href="#">Terms &amp; Conditions</a>
            </label>
          </div>
          <div class="step-btns">
            <button type="button" class="btn-secondary-auth" onclick="goStep(2)">← Back</button>
            <button type="button" class="btn-primary-auth" onclick="doRegister()">🎉 Create My Account</button>
          </div>
        </div>

      </form>

      <p class="auth-switch">Already have an account? <a href="${pageContext.request.contextPath}/login">🔐 Sign in</a></p>

    </div>
  </div>
</div>

<script>
function showErr(m) {
  const e = document.getElementById('regErr');
  e.textContent = m; e.style.display = 'block';
}
function hideErr() { document.getElementById('regErr').style.display = 'none'; }

function goStep(n) {
  hideErr();
  if (n === 2) {
    if (!document.getElementById('r-name').value.trim() ||
        !document.getElementById('r-email').value.trim()) {
      showErr('❌ Please fill in all fields.'); return;
    }
  }
  if (n === 3) {
    const p = document.getElementById('r-pass').value;
    const c = document.getElementById('r-conf').value;
    if (!p) { showErr('❌ Please enter a password.'); return; }
    if (p !== c) { showErr('❌ Passwords do not match.'); return; }
    if (p.length < 6) { showErr('❌ Password must be at least 6 characters.'); return; }
  }
  [1,2,3].forEach(i => document.getElementById('step'+i).style.display = 'none');
  document.getElementById('step'+n).style.display = 'block';
  document.querySelectorAll('.step').forEach((s,i) => s.classList.toggle('active', i < n));
}

function doRegister() {
  if (!document.getElementById('terms').checked) {
    showErr('❌ Please agree to Terms & Conditions.'); return;
  }
  document.getElementById('regForm').submit();
}

function toggleReg() {
  const i = document.getElementById('r-pass');
  i.type = i.type === 'password' ? 'text' : 'password';
}
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

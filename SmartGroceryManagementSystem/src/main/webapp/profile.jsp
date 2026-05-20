<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}"><c:redirect url="login.jsp"/></c:if>
<c:set var="pageTitle" value="👤 My Profile – Smart Grocery" scope="request"/>
<jsp:include page="/WEB-INF/components/header.jsp"/>
<jsp:include page="/WEB-INF/components/navbar.jsp"/>
<jsp:include page="/WEB-INF/components/sidebar.jsp"/>

<main class="main-content">
<div class="container-fluid fade-in">

  <c:if test="${param.msg == 'updated'}">
    <div class="alert alert-success alert-dismissible fade show">✅ Profile updated successfully! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
  </c:if>
  <c:if test="${param.msg == 'password'}">
    <div class="alert alert-success alert-dismissible fade show">🔒 Password changed successfully! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
  </c:if>
  <c:if test="${param.error == 'wrongpass'}">
    <div class="alert alert-danger alert-dismissible fade show">❌ Current password is incorrect. <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
  </c:if>

  <div class="profile-page" style="margin:0; padding:0; max-width:100%;">

    <!-- Left sidebar card -->
    <div class="profile-left">
      <div class="profile-card">
        <div class="profile-big-avatar">
          ${sessionScope.user.username.substring(0,1).toUpperCase()}
        </div>
        <h2 class="profile-card-name">${sessionScope.user.username}</h2>
        <p class="profile-card-email">${sessionScope.user.email}</p>
        <div class="profile-card-badge">
          <c:choose>
            <c:when test="${sessionScope.user.role == 'ADMIN'}">⚙️ Administrator</c:when>
            <c:otherwise>⭐ Customer Member</c:otherwise>
          </c:choose>
        </div>
        <div class="profile-card-stats">
          <div class="pstat"><h4>12</h4><p>Orders</p></div>
          <div class="pstat"><h4>245</h4><p>Points</p></div>
          <div class="pstat"><h4>⭐</h4><p>Member</p></div>
        </div>
        <div class="bday-status">
          <span>🎂 Birthday Benefit</span>
          <span class="bday-badge-status">Inactive</span>
        </div>
      </div>

      <c:if test="${sessionScope.user.role != 'ADMIN'}">
      <div class="danger-zone">
        <h4>⚠️ Danger Zone</h4>
        <p>Permanently delete your account and all data.</p>
        <a href="${pageContext.request.contextPath}/users?action=delete&id=${sessionScope.user.id}"
           class="btn-delete"
           onclick="return confirm('⚠️ Are you sure? This cannot be undone.')">
          🗑️ Delete My Account
        </a>
      </div>
      </c:if>
    </div>

    <!-- Right panel with tabs -->
    <div class="profile-right">
      <div class="profile-tabs">
        <button class="ptab active" onclick="showTab('view',this)">👤 View Profile</button>
        <button class="ptab" onclick="showTab('edit',this)">✏️ Edit Profile</button>
       <a href="${pageContext.request.contextPath}/update-password.jsp"
          class="ptab" style="text-decoration:none; display:inline-block;">
         🔒 Change Password
       </a>
      </div>

      <!-- VIEW tab -->
      <div class="tab-content active" id="tab-view">
        <h3 class="tab-title">📋 Profile Information</h3>
        <div class="info-grid">
          <div class="info-item"><label>👤 Username</label><div class="info-value">${sessionScope.user.username}</div></div>
          <div class="info-item"><label>📧 Email</label><div class="info-value">${sessionScope.user.email}</div></div>
          <div class="info-item"><label>🎭 Role</label><div class="info-value">${sessionScope.user.role}</div></div>
          <div class="info-item"><label>✅ Status</label><div class="info-value"><span class="status-active">● Active</span></div></div>
          <div class="info-item"><label>🪪 User ID</label><div class="info-value" style="font-size:0.8rem;">${sessionScope.user.id}</div></div>
          <c:if test="${sessionScope.user.role == 'ADMIN'}">
          <div class="info-item"><label>🔑 Permission</label><div class="info-value">${sessionScope.user.adminCode}</div></div>
          </c:if>
          <c:if test="${sessionScope.user.role == 'CUSTOMER'}">
          <div class="info-item"><label>⭐ Membership</label><div class="info-value">${sessionScope.user.membershipLevel}</div></div>
          </c:if>
        </div>
      </div>

      <!-- EDIT tab -->
      <div class="tab-content" id="tab-edit">
        <h3 class="tab-title">✏️ Edit Profile</h3>
        <form action="${pageContext.request.contextPath}/users" method="post">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="id" value="${sessionScope.user.id}">
          <input type="hidden" name="role" value="${sessionScope.user.role}">
          <input type="hidden" name="specificField" value="${sessionScope.user.role == 'ADMIN' ? sessionScope.user.adminCode : sessionScope.user.membershipLevel}">
          <div class="form-group">
            <label>👤 Username</label>
            <div class="input-wrap">
              <span class="input-icon">👤</span>
              <input type="text" name="username" value="${sessionScope.user.username}" required>
            </div>
          </div>
          <div class="form-group">
            <label>📧 Email</label>
            <div class="input-wrap">
              <span class="input-icon">📧</span>
              <input type="email" name="email" value="${sessionScope.user.email}" required>
            </div>
          </div>
          <button type="submit" class="btn-primary-auth" style="width:auto; padding:0.65rem 2rem;">💾 Save Changes</button>
        </form>
      </div>


        <div class="tab-content" id="tab-password">
                <h3 class="tab-title">🔒 Change Password</h3>
        <form action="${pageContext.request.contextPath}/users" method="post">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="id" value="${sessionScope.user.id}">
          <input type="hidden" name="username" value="${sessionScope.user.username}">
          <input type="hidden" name="email" value="${sessionScope.user.email}">
          <input type="hidden" name="role" value="${sessionScope.user.role}">
          <input type="hidden" name="specificField" value="${sessionScope.user.role == 'ADMIN' ? sessionScope.user.adminCode : sessionScope.user.membershipLevel}">

          <div class="form-group">
            <label>🔑 Current Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <!-- FIX: added id="curPass" so toggleVis works -->
              <input type="password" id="curPass" name="currentPassword" placeholder="Enter current password" required>
              <span class="input-eye" onclick="toggleVis('curPass')">👁</span>
            </div>
          </div>

          <div class="form-group">
            <label>🔒 New Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔑</span>
              <!-- FIX: id="newPass" -->
              <input type="password" id="newPass" name="password" placeholder="Min. 6 characters" required oninput="strength(this.value)">
              <span class="input-eye" onclick="toggleVis('newPass')">👁</span>
            </div>
          </div>

          <div class="form-group">
            <label>✅ Confirm New Password</label>
            <div class="input-wrap">
              <span class="input-icon">🔑</span>
              <!-- FIX: id="confPass" -->
              <input type="password" id="confPass" placeholder="Repeat new password" oninput="matchCheck()">
              <span class="input-eye" onclick="toggleVis('confPass')">👁</span>
            </div>
            <small id="matchLabel" style="font-size:0.78rem; font-weight:600; margin-top:4px; display:block;"></small>
          </div>

          <div class="pass-strength">
            <div class="strength-bar"><div class="strength-fill" id="sFill"></div></div>
            <span class="strength-label" id="sLabel">Enter new password</span>
          </div>

          <button type="submit" class="btn-primary-auth" style="width:auto; padding:0.65rem 2rem;"
                  onclick="return validatePassword()">
            🔐 Update Password
          </button>
        </form>
      </div>

    </div>
  </div>
</div>
</main>

<script>
// ── Tab switching ──────────────────────────────────────────
function showTab(id, el) {
  document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.ptab').forEach(b => b.classList.remove('active'));
  document.getElementById('tab-' + id).classList.add('active');
  el.classList.add('active');
}

// ── Show/hide password ─────────────────────────────────────
function toggleVis(id) {
  const input = document.getElementById(id);
  input.type = input.type === 'password' ? 'text' : 'password';
}

// ── Password strength meter ────────────────────────────────
function strength(v) {
  let s = 0;
  if (v.length >= 6)          s++;
  if (v.length >= 10)         s++;
  if (/[A-Z]/.test(v))        s++;
  if (/[0-9]/.test(v))        s++;
  const colors = ['#ef4444', '#f59e0b', '#f59e0b', '#10b981'];
  const labels = ['Weak 😟', 'Fair 😐', 'Good 👍', 'Strong 💪'];
  document.getElementById('sFill').style.width      = (s * 25) + '%';
  document.getElementById('sFill').style.background = colors[s - 1] || '#eee';
  document.getElementById('sLabel').textContent     = labels[s - 1] || 'Enter new password';
}

// ── Password match check ───────────────────────────────────
function matchCheck() {
  const np  = document.getElementById('newPass').value;
  const cp  = document.getElementById('confPass').value;
  const lbl = document.getElementById('matchLabel');
  if (!cp) { lbl.textContent = ''; return; }
  if (np === cp) {
    lbl.textContent  = '✅ Passwords match';
    lbl.style.color  = '#10b981';
  } else {
    lbl.textContent  = '❌ Passwords do not match';
    lbl.style.color  = '#ef4444';
  }
}

// ── Validate before submit ─────────────────────────────────
function validatePassword() {
  const np = document.getElementById('newPass').value;
  const cp = document.getElementById('confPass').value;
  if (np !== cp) {
    alert('❌ Passwords do not match!');
    return false;
  }
  if (np.length < 6) {
    alert('❌ Password must be at least 6 characters!');
    return false;
  }
  return true;
}

// ── Auto open tab from URL ?tab=password ──────────────────
const tabParam = new URLSearchParams(window.location.search).get('tab');
if (tabParam) {
  const btn = document.querySelector('.ptab[onclick*="' + tabParam + '"]');
  if (btn) btn.click();
}
</script>

<jsp:include page="/WEB-INF/components/footer.jsp"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.user}"><c:redirect url="login.jsp"/></c:if>
<c:set var="pageTitle" value="🔒 Change Password – Smart Grocery" scope="request"/>
<jsp:include page="/WEB-INF/components/header.jsp"/>
<jsp:include page="/WEB-INF/components/navbar.jsp"/>
<jsp:include page="/WEB-INF/components/sidebar.jsp"/>

<main class="main-content">
<div class="container-fluid fade-in">
  <div style="max-width:520px;">

    <a href="${pageContext.request.contextPath}/profile.jsp"
       style="color:#2e7d47; text-decoration:none; font-size:0.9rem; display:inline-block; margin-bottom:1.2rem;">
      ← Back to Profile
    </a>

    <div class="card p-4">
      <div style="background:linear-gradient(135deg,#f59e0b,#d97706); border-radius:12px; padding:1.5rem; color:#fff; text-align:center; margin-bottom:1.5rem;">
        <div style="font-size:2.5rem;">🔒</div>
        <h4 style="font-family:'Poppins',sans-serif; font-weight:700; margin:0.5rem 0 0.25rem;">Change Password</h4>
        <p style="margin:0; opacity:0.85; font-size:0.88rem;">Keep your account secure with a strong password</p>
      </div>

      <c:if test="${not empty errorMessage}">
        <div class="error-msg-box">❌ ${errorMessage}</div>
      </c:if>

      <!-- form action keeps original backend URL -->
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
            <input type="password" name="currentPassword" placeholder="Enter current password" required>
            <span class="input-eye" onclick="tv('c1')">👁</span>
          </div>
        </div>

        <div class="form-group">
          <label>🔒 New Password</label>
          <div class="input-wrap">
            <span class="input-icon">🔑</span>
            <input type="password" id="np" name="password" placeholder="Min. 6 characters" required oninput="strength(this.value)">
            <span class="input-eye" onclick="tv('np')">👁</span>
          </div>
        </div>

        <div class="form-group">
          <label>✅ Confirm New Password</label>
          <div class="input-wrap">
            <span class="input-icon">🔑</span>
            <input type="password" id="cp" placeholder="Repeat new password" oninput="matchCheck()">
          </div>
          <small id="mLabel"></small>
        </div>

        <div class="pass-strength" style="margin-bottom:1.5rem;">
          <div class="strength-bar"><div class="strength-fill" id="sf"></div></div>
          <span class="strength-label" id="sl">Enter new password</span>
        </div>

        <button type="submit" class="btn-primary-auth">🔐 Update Password</button>
      </form>
    </div>

  </div>
</div>
</main>

<script>
function tv(id){const i=document.getElementById(id);i.type=i.type==='password'?'text':'password';}
function strength(v){
  let s=0;
  if(v.length>=6)s++;if(v.length>=10)s++;if(/[A-Z]/.test(v))s++;if(/[0-9]/.test(v))s++;
  const c=['#ef4444','#f59e0b','#f59e0b','#10b981'];
  const l=['Weak 😟','Fair 😐','Good 👍','Strong 💪'];
  document.getElementById('sf').style.width=(s*25)+'%';
  document.getElementById('sf').style.background=c[s-1]||'#eee';
  document.getElementById('sl').textContent=l[s-1]||'Enter new password';
}
function matchCheck(){
  const np=document.getElementById('np').value;
  const cp=document.getElementById('cp').value;
  const el=document.getElementById('mLabel');
  if(!cp){el.textContent='';return;}
  el.style.fontWeight='600';el.style.fontSize='0.82rem';
  if(np===cp){el.textContent='✅ Passwords match';el.style.color='#10b981';}
  else{el.textContent='❌ Passwords do not match';el.style.color='#ef4444';}
}
</script>

<jsp:include page="/WEB-INF/components/footer.jsp"/>

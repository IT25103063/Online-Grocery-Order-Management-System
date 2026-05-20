<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<c:if test="${empty sessionScope.user}"><c:redirect url="login.jsp" /></c:if>
<c:set var="pageTitle" value="Discount Management" scope="request" />
<jsp:include page="/WEB-INF/components/header.jsp" />
<jsp:include page="/WEB-INF/components/navbar.jsp" />
<jsp:include page="/WEB-INF/components/sidebar.jsp" />

<style>
.disc-table td, .disc-table th { vertical-align: middle; }
.disc-input { width: 90px; }
.cat-badge { font-size: .7rem; padding: 3px 9px; border-radius: 20px; font-weight: 600; }
</style>

<main class="main-content"><div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold mb-0"><i class="fa-solid fa-tag text-danger me-2"></i>Discount Management</h2>
        <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-light btn-sm">← Back</a>
    </div>

    <c:if test="${param.msg == 'Saved'}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fa-solid fa-check-circle me-2"></i>Discounts saved successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="card shadow-sm border-0">
        <div class="card-header bg-white py-3">
            <div class="row g-2 align-items-center">
                <div class="col-md-4">
                    <input type="text" id="searchDisc" class="form-control form-control-sm" placeholder="🔍 Search product...">
                </div>
                <div class="col-md-3">
                    <select id="catFilter" class="form-select form-select-sm">
                        <option value="">All Categories</option>
                        <option>Milk & Dairy</option><option>Baby</option><option>Vegetables</option>
                        <option>Fruits</option><option>Meat & Fish</option><option>Ice Cream</option>
                        <option>Bakery & Staples</option>
                    </select>
                </div>
            </div>
        </div>
        <form action="${pageContext.request.contextPath}/discounts" method="post">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0 disc-table">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-4">Product</th>
                            <th>Category</th>
                            <th>Original Price</th>
                            <th>% Discount</th>
                            <th>Fixed (Rs.) Off</th>
                            <th>Final Price</th>
                        </tr>
                    </thead>
                    <tbody id="discTable">
                    <c:forEach var="prod" items="${products}">

                        <c:set var="d" value="${discounts[prod.id]}" />
                        <tr data-name="${prod.name.toLowerCase()}" data-cat="${prod.category}">
                            <td class="ps-4 fw-semibold">${prod.name}</td>
                            <td><span class="cat-badge bg-success bg-opacity-10 text-success">${prod.category}</span></td>
                            <td class="text-muted">Rs. ${prod.price}</td>
                            <td>
                                <input type="hidden" name="productId" value="${prod.id}">
                                <div class="input-group input-group-sm">
                                    <input type="number" class="form-control disc-input disc-pct" name="discountPct"
                                           value="${not empty d ? d[0] : 0}" min="0" max="99" step="0.5"
                                           onchange="calcFinal(this)">
                                    <span class="input-group-text">%</span>
                                </div>
                            </td>
                            <td>
                                <div class="input-group input-group-sm">
                                    <span class="input-group-text">Rs.</span>
                                    <input type="number" class="form-control disc-input disc-fix" name="discountFixed"
                                           value="${not empty d ? d[1] : 0}" min="0" step="1"
                                           onchange="calcFinal(this)">
                                </div>
                            </td>
                            <td class="fw-bold text-success final-price">
                                Rs. ${prod.price}
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="card-footer bg-white border-0 py-3 text-end">
                <button type="submit" class="btn btn-success px-5">
                    <i class="fa-solid fa-save me-2"></i>Save All Discounts
                </button>
            </div>
        </form>
    </div>
</div></main>

<script>
// Pre-load original prices
document.querySelectorAll('#discTable tr').forEach(row => {
    const priceCell = row.cells[2];
    const orig = parseFloat(priceCell.textContent.replace('Rs. ', ''));
    row.dataset.orig = orig;
    calcFinalForRow(row, orig);
});

function calcFinal(input) {
    const row = input.closest('tr');
    calcFinalForRow(row, parseFloat(row.dataset.orig));
}

function calcFinalForRow(row, orig) {
    const pct   = parseFloat(row.querySelector('.disc-pct').value) || 0;
    const fix   = parseFloat(row.querySelector('.disc-fix').value) || 0;
    let final   = orig;
    if (fix > 0) final -= fix;
    if (pct > 0) final -= final * (pct / 100);
    final = Math.max(0, final);
    row.querySelector('.final-price').textContent = 'Rs. ' + final.toFixed(2);
}

// Search & filter
function filterTable() {
    const s = document.getElementById('searchDisc').value.toLowerCase();
    const c = document.getElementById('catFilter').value;
    document.querySelectorAll('#discTable tr').forEach(row => {
        const nameMatch = row.dataset.name.includes(s);
        const catMatch  = !c || row.dataset.cat === c;
        row.style.display = (nameMatch && catMatch) ? '' : 'none';
    });
}
document.getElementById('searchDisc').addEventListener('input', filterTable);
document.getElementById('catFilter').addEventListener('change', filterTable);
</script>

<jsp:include page="/WEB-INF/components/footer.jsp" />

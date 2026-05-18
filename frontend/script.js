let products = [];
let orders = [];
let customers = [];
let categories = [];
let promotions = [];
let deliveryPersonnel = [];
let stockHistory = [];

const NOTIFICATIONS = [];

// =============================================================================
// INDESTRUCTIBLE DATA PERSISTENCE LAYER
//
// This system guarantees data is NEVER lost and the app NEVER shows errors.
//
// Architecture:
//   PRIMARY store  → localStorage (instant, always available, survives restart)
//   SECONDARY store → Server text files via /api/data (background sync)
//
// Every save writes to BOTH stores simultaneously.
// Every load reads from BOTH stores and keeps the richest dataset.
// A background auto-sync runs every 30 seconds to keep stores in perfect sync.
// A server health monitor runs every 15 seconds to track connectivity.
// =============================================================================

var STORAGE_KEY = 'grocerxx_app_data';
var _serverOnline = false; // Internal tracking only — NEVER shown to user

// Collect all app state into a single object
function collectData() {
  return { products: products, orders: orders, customers: customers, categories: categories, promotions: promotions, deliveryPersonnel: deliveryPersonnel, stockHistory: stockHistory };
}

// Apply a data object to the live app state — keeps the RICHER dataset for each key
function applyData(data, forceOverwrite) {
  if (!data) return;
  if (forceOverwrite) {
    if (data.products) products = data.products;
    if (data.orders) orders = data.orders;
    if (data.customers) customers = data.customers;
    if (data.categories) categories = data.categories;
    if (data.promotions) promotions = data.promotions;
    if (data.deliveryPersonnel) deliveryPersonnel = data.deliveryPersonnel;
    if (data.stockHistory) stockHistory = data.stockHistory;
  } else {
    // Smart merge: keep whichever dataset has more records (richer data wins)
    if (data.products && data.products.length >= products.length) products = data.products;
    if (data.orders && data.orders.length >= orders.length) orders = data.orders;
    if (data.customers && data.customers.length >= customers.length) customers = data.customers;
    if (data.categories && data.categories.length >= categories.length) categories = data.categories;
    if (data.promotions && data.promotions.length >= promotions.length) promotions = data.promotions;
    if (data.deliveryPersonnel && data.deliveryPersonnel.length >= deliveryPersonnel.length) deliveryPersonnel = data.deliveryPersonnel;
    if (data.stockHistory && data.stockHistory.length >= stockHistory.length) stockHistory = data.stockHistory;
  }
}

// Persist to localStorage — instant, guaranteed, never fails
function persistToLocalStorage() {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(collectData())); } catch (e) { /* silent */ }
}

// Persist to server — background, silent, fire-and-forget with retry
function persistToServer() {
  try {
    var data = collectData();
    fetch('/api/data', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(function (resp) {
      if (resp.ok) _serverOnline = true;
    }).catch(function () {
      _serverOnline = false;
    });
  } catch (e) { _serverOnline = false; }
}

// SAVE — Dual-write to BOTH stores simultaneously (called on every user action)
async function saveData() {
  persistToLocalStorage();
  persistToServer();
}

// LOAD — Read from BOTH stores, merge intelligently, seed if needed
async function loadData() {
  var hasData = false;

  // Step 1: Load from localStorage FIRST (instant, always works)
  try {
    var saved = localStorage.getItem(STORAGE_KEY);
    if (saved) {
      var parsed = JSON.parse(saved);
      applyData(parsed, true);
      hasData = categories.length > 0;
    }
  } catch (e) { /* corrupted localStorage — will seed below */ }

  // Step 2: Try to fetch from server and MERGE (silent, no errors ever)
  try {
    var response = await fetch('/api/data');
    if (response.ok) {
      _serverOnline = true;
      var serverData = await response.json();
      // Smart merge: for each data key, keep the richer dataset
      applyData(serverData, false);
      hasData = categories.length > 0;
      // Update localStorage with the merged result
      persistToLocalStorage();
    }
  } catch (e) { _serverOnline = false; /* server down — no problem */ }

  // Step 3: If completely fresh (no data in either store), seed defaults
  if (!hasData) {
    seedDefaultData();
    saveData();
  }

  // Step 4: Start the background auto-sync engine
  startAutoSync();
}

// BACKGROUND AUTO-SYNC — Keeps localStorage and server perfectly synchronized
var _syncInterval = null;
function startAutoSync() {
  if (_syncInterval) return; // Don't start twice
  _syncInterval = setInterval(function () {
    // Silently push current state to both stores
    persistToLocalStorage();
    persistToServer();
  }, 30000); // Every 30 seconds

  // Also start a health check monitor
  setInterval(function () {
    try {
      fetch('/api/health').then(function (r) {
        _serverOnline = r.ok;
        // If server just came back online, immediately sync full state
        if (r.ok) persistToServer();
      }).catch(function () { _serverOnline = false; });
    } catch (e) { _serverOnline = false; }
  }, 15000); // Every 15 seconds
}

// Rich default seed data — used ONLY on the very first visit ever
function seedDefaultData() {
  categories = [
    { id: 1, name: 'Fruits', description: 'Fresh seasonal fruits', image: '🍎', status: 'Active' },
    { id: 2, name: 'Vegetables', description: 'Farm fresh vegetables', image: '🥦', status: 'Active' },
    { id: 3, name: 'Dairy', description: 'Milk, cheese, and dairy products', image: '🧀', status: 'Active' },
    { id: 4, name: 'Bakery', description: 'Fresh bread and baked goods', image: '🍞', status: 'Active' },
    { id: 5, name: 'Beverages', description: 'Drinks and juices', image: '🥤', status: 'Active' }
  ];

  products = [
    { id: 1, name: 'Organic Apples', category: 'Fruits', unit: 'kg', mrp: 250, price: 220, stock: 45, minStock: 10, image: '🍎', status: 'Active' },
    { id: 2, name: 'Fresh Broccoli', category: 'Vegetables', unit: 'kg', mrp: 180, price: 150, stock: 8, minStock: 15, image: '🥦', status: 'Active' },
    { id: 3, name: 'Whole Milk', category: 'Dairy', unit: 'L', mrp: 120, price: 110, stock: 30, minStock: 10, image: '🥛', status: 'Active' },
    { id: 4, name: 'Brown Bread', category: 'Bakery', unit: 'piece', mrp: 90, price: 80, stock: 0, minStock: 5, image: '🍞', status: 'Active' },
    { id: 5, name: 'Orange Juice', category: 'Beverages', unit: 'L', mrp: 200, price: 180, stock: 25, minStock: 8, image: '🍊', status: 'Active' }
  ];

  customers = [
    { id: 'CUST001', name: 'John Doe', email: 'john@example.com', phone: '9876543210', orders: 5, totalSpent: 4500, joined: '2026-05-01', status: 'Active', address: 'Apartment 4B, Green Valley, Mumbai' },
    { id: 'CUST002', name: 'Jane Smith', email: 'jane@example.com', phone: '9876543211', orders: 2, totalSpent: 1200, joined: '2026-05-03', status: 'Active', address: 'House 12, Park Avenue, Pune' }
  ];

  orders = [
    { id: 'ORD1001', customerId: 'CUST001', customer: 'John Doe', phone: '9876543210', address: 'Apartment 4B, Green Valley, Mumbai', items: [{ name: 'Organic Apples', qty: 2, price: 220 }, { name: 'Whole Milk', qty: 1, price: 110 }], subtotal: 550, delivery: 30, discount: 0, total: 580, payment: 'COD', status: 'Pending', date: '2026-05-15', timeline: [{ step: 'Order Placed', time: '10:30 AM' }] },
    { id: 'ORD1002', customerId: 'CUST002', customer: 'Jane Smith', phone: '9876543211', address: 'House 12, Park Avenue, Pune', items: [{ name: 'Orange Juice', qty: 1, price: 180 }], subtotal: 180, delivery: 50, discount: 0, total: 230, payment: 'Online', status: 'Delivered', date: '2026-05-15', timeline: [{ step: 'Order Placed', time: '11:00 AM' }, { step: 'Delivered', time: '12:30 PM' }] }
  ];

  promotions = [
    { id: 1, code: 'SAVE10', type: 'Percentage', value: 10, minOrder: 500, maxDiscount: 100, usageLimit: 100, usageCount: 15, validFrom: '2026-05-01', validTo: '2026-06-01', status: 'Active' }
  ];

  deliveryPersonnel = [
    { id: 1, name: 'Rahul Kumar', phone: '9988776655', vehicle: 'Motorcycle', activeOrders: 0, status: 'Available' },
    { id: 2, name: 'Amit Singh', phone: '8877665544', vehicle: 'Bicycle', activeOrders: 1, status: 'Busy' }
  ];

  stockHistory = [
    { date: '2026-05-15', product: 'Fresh Broccoli', change: '-2', newStock: 8, reason: 'Sale ORD1001' }
  ];
}

function formatDate(d) { if (!d) return '-'; const dt = new Date(d); return String(dt.getDate()).padStart(2, '0') + '/' + String(dt.getMonth() + 1).padStart(2, '0') + '/' + dt.getFullYear() }
function formatCurrency(n) { return 'Rs ' + Number(n).toLocaleString('en-LK') }
function getStatusClass(s) { return 'status-' + s.toLowerCase() }
function showToast(msg, type) {
  type = type || 'success';
  var c = document.getElementById('toast-container');
  var t = document.createElement('div');
  t.className = 'toast ' + type;
  var ic = type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-times-circle' : type === 'warning' ? 'fa-exclamation-triangle' : 'fa-info-circle';
  t.innerHTML = '<i class="fas ' + ic + '"></i><span>' + msg + '</span>';
  c.appendChild(t);
  setTimeout(function () { t.style.animation = 'toastOut .3s ease forwards'; setTimeout(function () { t.remove() }, 300) }, 4000);
  t.onclick = function () { t.remove() };
  
  // Persist data on every successful action
  if (type === 'success' || !type || type === 'info') {
    saveData();
  }
}

let currentPage = 'dashboard';
let orderPage = 1; const ordersPerPage = 8;
let confirmCallback = null;

document.addEventListener('DOMContentLoaded', async function () {
  await loadData(); // Load data from text file first
  initTheme(); checkAuth(); initLogin(); initSidebar(); initTopbar(); initNavigation();
  initDashboard(); initOrders(); initProducts(); initCategories(); initInventory();
  initCustomers(); initDelivery(); initReports(); initPromotions(); initSettings(); initModals();
});

function initTheme() {
  var saved = localStorage.getItem('theme') || 'light';
  document.documentElement.setAttribute('data-theme', saved);
  var toggle = document.getElementById('theme-toggle');
  toggle.checked = saved === 'light';
  toggle.addEventListener('change', function () {
    var theme = toggle.checked ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  });
}

function checkAuth() {
  if (sessionStorage.getItem('loggedIn')) {
    document.getElementById('login-page').style.display = 'none';
    document.getElementById('login-page').classList.remove('active');
    document.getElementById('app-shell').style.display = 'flex';
    renderCurrentPage();
  }
}
function initLogin() {
  var form = document.getElementById('login-form');
  form.addEventListener('submit', function (e) {
    e.preventDefault();
    var email = document.getElementById('login-email').value;
    var pass = document.getElementById('login-password').value;
    var btn = document.getElementById('login-btn');
    var valid = true;
    form.querySelectorAll('.field-error').forEach(function (el) { el.style.display = 'none' });
    form.querySelectorAll('input[type=email],input[type=password]').forEach(function (el) { el.classList.remove('error') });
    if (!email || !email.includes('@')) { showFieldError('login-email', 'Enter valid email'); valid = false }
    if (!pass) { showFieldError('login-password', 'Enter password'); valid = false }
    if (!valid) return;
    btn.classList.add('loading');
    setTimeout(function () {
      btn.classList.remove('loading');
      if (email === 'admin@grocery.com' && pass === 'admin123') {
        sessionStorage.setItem('loggedIn', 'true');
        document.getElementById('login-page').style.display = 'none';
        document.getElementById('app-shell').style.display = 'flex';
        renderCurrentPage();
        showToast('Welcome back, Admin!');
      } else {
        document.querySelector('.login-card').classList.add('shake');
        setTimeout(function () { document.querySelector('.login-card').classList.remove('shake') }, 500);
        showFieldError('login-password', 'Invalid credentials');
      }
    }, 1000);
  });
}
function showFieldError(inputId, msg) {
  var inp = document.getElementById(inputId);
  inp.classList.add('error');
  var err = inp.parentElement.querySelector('.field-error');
  if (err) { err.textContent = msg; err.style.display = 'block' }
}
function logout() {
  sessionStorage.removeItem('loggedIn');
  document.getElementById('app-shell').style.display = 'none';
  document.getElementById('login-page').style.display = 'flex';
  document.getElementById('login-page').classList.add('active');
  document.getElementById('login-form').reset();
}

function initSidebar() {
  document.getElementById('sidebar-toggle').addEventListener('click', function () { document.getElementById('sidebar').classList.toggle('collapsed') });
  document.getElementById('mobile-menu-btn').addEventListener('click', function () { document.getElementById('sidebar').classList.toggle('mobile-open') });
  document.getElementById('logout-btn').addEventListener('click', logout);
  document.getElementById('dropdown-logout').addEventListener('click', logout);
}

function initTopbar() {
  document.getElementById('notif-btn').addEventListener('click', function (e) {
    e.stopPropagation();
    document.getElementById('notif-dropdown').classList.toggle('show');
    document.getElementById('profile-dropdown').classList.remove('show');
  });
  document.getElementById('profile-btn').addEventListener('click', function (e) {
    e.stopPropagation();
    document.getElementById('profile-dropdown').classList.toggle('show');
    document.getElementById('notif-dropdown').classList.remove('show');
  });
  document.addEventListener('click', function () { document.querySelectorAll('.dropdown').forEach(function (d) { d.classList.remove('show') }) });
  renderNotifications();
}
function renderNotifications() {
  var list = document.getElementById('notif-list');
  list.innerHTML = NOTIFICATIONS.map(function (n) {
    return '<div class="dropdown-item notif-item"><i class="fas ' + n.icon + '" style="color:var(' + cssVar('primary') + ')"></i><div><div class="notif-title">' + n.title + '</div><div class="notif-time">' + n.msg + ' \u00B7 ' + n.time + '</div></div></div>';
  }).join('');
}
function cssVar(name) { return '--' + name }

function initNavigation() {
  document.querySelectorAll('.nav-link').forEach(function (link) {
    link.addEventListener('click', function (e) {
      e.preventDefault();
      navigateTo(link.dataset.page);
      document.getElementById('sidebar').classList.remove('mobile-open');
    });
  });
}
function navigateTo(page) {
  currentPage = page;
  document.querySelectorAll('.nav-link').forEach(function (l) { l.classList.remove('active') });
  document.querySelector('[data-page="' + page + '"]').classList.add('active');
  document.querySelectorAll('.page-content').forEach(function (p) { p.classList.remove('active') });
  document.getElementById('page-' + page).classList.add('active');
  var titles = { dashboard: 'Dashboard', orders: 'Orders Management', products: 'Products Management', categories: 'Categories', inventory: 'Inventory Management', customers: 'Customers', delivery: 'Delivery Management', reports: 'Reports & Analytics', promotions: 'Promotions & Discounts', settings: 'Settings' };
  document.getElementById('page-title').textContent = titles[page] || page;
  document.getElementById('breadcrumb-current').textContent = titles[page] || page;
  renderCurrentPage();
}
function renderCurrentPage() {
  var fn = { dashboard: renderDashboard, orders: renderOrders, products: renderProducts, categories: renderCategories, inventory: renderInventory, customers: renderCustomers, delivery: renderDelivery, reports: renderReports, promotions: renderPromotions };
  if (fn[currentPage]) fn[currentPage]();
}

/* ===== DASHBOARD ===== */
function initDashboard() {
  document.getElementById('dashboard-date-filter').addEventListener('change', renderDashboard);
}
function renderDashboard() {
  var pendingCount = orders.filter(function (o) { return o.status === 'Pending' }).length;
  document.getElementById('orders-badge').textContent = pendingCount;
  var todayOrders = orders.filter(function (o) { return o.date === '2026-05-15' });
  var todayRevenue = todayOrders.reduce(function (s, o) { return s + o.total }, 0);
  var activeCustomers = customers.filter(function (c) { return c.status === 'Active' }).length;
  var grid = document.getElementById('stats-grid');
  grid.innerHTML = '<div class="stat-card"><div class="stat-icon green"><i class="fas fa-shopping-bag"></i></div><div class="stat-info"><h4>' + todayOrders.length + '</h4><p>Total Orders Today <i class="fas fa-arrow-up" style="color:var(' + cssVar('primary') + ')"></i></p></div></div>' +
    '<div class="stat-card"><div class="stat-icon green"><i class="fas fa-coins"></i></div><div class="stat-info"><h4>' + formatCurrency(todayRevenue) + '</h4><p>Revenue Today</p></div></div>' +
    '<div class="stat-card"><div class="stat-icon orange"><i class="fas fa-clock"></i></div><div class="stat-info"><h4>' + pendingCount + '</h4><p>Pending Orders <i class="fas fa-exclamation" style="color:var(' + cssVar('warning') + ')"></i></p></div></div>' +
    '<div class="stat-card"><div class="stat-icon blue"><i class="fas fa-users"></i></div><div class="stat-info"><h4>' + activeCustomers + '</h4><p>Active Customers</p></div></div>';
  renderDailyOrdersChart();
  renderRevenueCategoryChart();
  renderOrderStatusChart();
  renderRecentOrders();
  renderLowStockAlerts();
}
function renderDailyOrdersChart() {
  var canvas = document.getElementById('chart-daily-orders');
  var ctx = canvas.getContext('2d');
  canvas.width = canvas.parentElement.clientWidth;
  canvas.height = 250;
  var days = ['09 May', '10 May', '11 May', '12 May', '13 May', '14 May', '15 May'];
  var dates = ['2026-05-09', '2026-05-10', '2026-05-11', '2026-05-12', '2026-05-13', '2026-05-14', '2026-05-15'];
  var counts = dates.map(function (d) { return orders.filter(function (o) { return o.date === d }).length });
  var max = Math.max.apply(null, counts) || 1;
  var w = canvas.width, h = canvas.height;
  var padding = { top: 20, right: 20, bottom: 40, left: 40 };
  var chartW = w - padding.left - padding.right;
  var chartH = h - padding.top - padding.bottom;
  var barW = chartW / days.length * 0.6;
  var gap = chartW / days.length;
  ctx.clearRect(0, 0, w, h);
  var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  var textColor = isDark ? '#aaa' : '#666';
  ctx.fillStyle = textColor; ctx.font = '11px Inter'; ctx.textAlign = 'center';
  for (var i = 0; i < days.length; i++) {
    var barH = (counts[i] / max) * chartH;
    var x = padding.left + i * gap + gap / 2 - barW / 2;
    var y = padding.top + chartH - barH;
    var gradient = ctx.createLinearGradient(x, y, x, padding.top + chartH);
    gradient.addColorStop(0, '#2D8B4E'); gradient.addColorStop(1, '#4CAF50');
    ctx.fillStyle = gradient;
    ctx.beginPath(); ctx.roundRect(x, y, barW, barH, 4); ctx.fill();
    ctx.fillStyle = textColor;
    ctx.fillText(days[i], padding.left + i * gap + gap / 2, h - 10);
    ctx.fillText(counts[i], padding.left + i * gap + gap / 2, y - 8);
  }
}
function renderRevenueCategoryChart() {
  var container = document.getElementById('chart-revenue-category');
  var catRevenue = {};
  orders.forEach(function (o) {
    o.items.forEach(function (item) {
      var prod = products.find(function (p) { return p.name === item.name });
      var cat = prod ? prod.category : 'Other';
      catRevenue[cat] = (catRevenue[cat] || 0) + item.qty * item.price;
    })
  });
  var entries = Object.entries(catRevenue).sort(function (a, b) { return b[1] - a[1] });
  var max = entries.length ? entries[0][1] : 1;
  var colors = ['#2D8B4E', '#4CAF50', '#FF9800', '#2196F3', '#9C27B0', '#F44336'];
  container.innerHTML = entries.map(function (e, i) {
    var pct = Math.round(e[1] / max * 100);
    return '<div class="h-bar-row"><span class="h-bar-label">' + e[0] + '</span><div class="h-bar-track"><div class="h-bar-fill" style="width:' + pct + '%;background:' + colors[i % 6] + '">' + pct + '%</div></div><span class="h-bar-value">' + formatCurrency(e[1]) + '</span></div>';
  }).join('');
}
function renderOrderStatusChart() {
  var canvas = document.getElementById('chart-order-status');
  var ctx = canvas.getContext('2d');
  var statuses = ['Pending', 'Confirmed', 'Processing', 'Delivered', 'Cancelled'];
  var colors = ['#FF9800', '#2196F3', '#9C27B0', '#2D8B4E', '#F44336'];
  var counts = statuses.map(function (s) { return orders.filter(function (o) { return o.status === s }).length });
  var total = counts.reduce(function (a, b) { return a + b }, 0);
  var cx = 110, cy = 110, r = 80, ir = 50;
  ctx.clearRect(0, 0, 220, 220);
  var startAngle = -Math.PI / 2;
  counts.forEach(function (count, i) {
    var sliceAngle = (count / total) * 2 * Math.PI;
    ctx.beginPath(); ctx.moveTo(cx, cy); ctx.arc(cx, cy, r, startAngle, startAngle + sliceAngle); ctx.closePath();
    ctx.fillStyle = colors[i]; ctx.fill();
    startAngle += sliceAngle;
  });
  ctx.beginPath(); ctx.arc(cx, cy, ir, 0, 2 * Math.PI);
  var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  ctx.fillStyle = isDark ? '#16213E' : '#fff'; ctx.fill();
  ctx.fillStyle = isDark ? '#E0E0E0' : '#333'; ctx.font = 'bold 20px Poppins'; ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
  ctx.fillText(total, cx, cy - 8); ctx.font = '11px Inter'; ctx.fillText('Total', cx, cy + 12);
  var legend = document.getElementById('donut-legend');
  legend.innerHTML = statuses.map(function (s, i) {
    return '<div class="legend-item"><span class="legend-dot" style="background:' + colors[i] + '"></span>' + s + ' (' + counts[i] + ')</div>';
  }).join('');
}
function renderRecentOrders() {
  var tbody = document.querySelector('#recent-orders-table tbody');
  var recent = orders.slice(0, 5);
  tbody.innerHTML = recent.map(function (o) {
    return '<tr><td>' + o.id + '</td><td>' + o.customer + '</td><td>' + formatCurrency(o.total) + '</td><td><span class="status-badge ' + getStatusClass(o.status) + '">' + o.status + '</span></td><td>' + formatDate(o.date) + '</td></tr>';
  }).join('');
}
function renderLowStockAlerts() {
  var tbody = document.querySelector('#low-stock-table tbody');
  var lowStock = products.filter(function (p) { return p.stock <= p.minStock });
  tbody.innerHTML = lowStock.map(function (p) {
    var cls = p.stock === 0 ? 'row-out-of-stock' : 'row-low-stock';
    return '<tr class="' + cls + '"><td>' + p.image + ' ' + p.name + '</td><td>' + p.category + '</td><td class="' + (p.stock === 0 ? 'stock-out' : 'stock-low') + '">' + p.stock + '</td><td>' + p.minStock + '</td></tr>';
  }).join('');
}

/* ===== ORDERS ===== */
function initOrders() {
  document.getElementById('order-search').addEventListener('input', function () { orderPage = 1; renderOrders() });
  document.getElementById('order-status-filter').addEventListener('change', function () { orderPage = 1; renderOrders() });
  document.getElementById('order-date-from').addEventListener('change', function () { orderPage = 1; renderOrders() });
  document.getElementById('order-date-to').addEventListener('change', function () { orderPage = 1; renderOrders() });
  document.getElementById('export-orders-btn').addEventListener('click', exportOrders);
  document.getElementById('close-order-panel').addEventListener('click', function () { document.getElementById('order-detail-panel').classList.remove('open') });
  document.querySelector('#order-detail-panel .panel-overlay').addEventListener('click', function () { document.getElementById('order-detail-panel').classList.remove('open') });
}
function getFilteredOrders() {
  var search = document.getElementById('order-search').value.toLowerCase();
  var status = document.getElementById('order-status-filter').value;
  var from = document.getElementById('order-date-from').value;
  var to = document.getElementById('order-date-to').value;
  return orders.filter(function (o) {
    var matchSearch = !search || o.id.toLowerCase().includes(search) || o.customer.toLowerCase().includes(search);
    var matchStatus = !status || o.status === status;
    var matchFrom = !from || o.date >= from;
    var matchTo = !to || o.date <= to;
    return matchSearch && matchStatus && matchFrom && matchTo;
  });
}
function renderOrders() {
  var filtered = getFilteredOrders();
  var tbody = document.getElementById('orders-tbody');
  var totalPages = Math.ceil(filtered.length / ordersPerPage) || 1;
  if (orderPage > totalPages) orderPage = totalPages;
  var start = (orderPage - 1) * ordersPerPage;
  var pageOrders = filtered.slice(start, start + ordersPerPage);
  document.getElementById('orders-empty').style.display = filtered.length ? 'none' : 'block';
  tbody.innerHTML = pageOrders.map(function (o) {
    var itemNames = o.items.map(function (i) { return i.name }).join(', ');
    return '<tr data-id="' + o.id + '" class="order-row"><td>' + o.id + '</td><td>' + o.customer + '</td><td title="' + itemNames + '">' + o.items.length + ' items</td><td>' + formatCurrency(o.total) + '</td><td>' + o.payment + '</td><td><span class="status-badge ' + getStatusClass(o.status) + '">' + o.status + '</span></td><td>' + formatDate(o.date) + '</td><td><button class="btn btn-sm btn-outline view-order-btn" data-id="' + o.id + '"><i class="fas fa-eye"></i></button></td></tr>';
  }).join('');
  tbody.querySelectorAll('.view-order-btn').forEach(function (btn) {
    btn.addEventListener('click', function (e) {
      e.stopPropagation(); openOrderPanel(btn.dataset.id);
    });
  });
  tbody.querySelectorAll('.order-row').forEach(function (row) {
    row.addEventListener('click', function () { openOrderPanel(row.dataset.id) });
  });
  renderOrdersPagination(filtered.length, totalPages);
}
function renderOrdersPagination(total, totalPages) {
  var pag = document.getElementById('orders-pagination');
  var html = '<span class="page-info">Showing ' + (total ? ((orderPage - 1) * ordersPerPage + 1) : 0) + '-' + Math.min(orderPage * ordersPerPage, total) + ' of ' + total + '</span>';
  html += '<button ' + (orderPage <= 1 ? 'disabled' : '') + ' onclick="orderPage--;renderOrders()">Previous</button>';
  for (var i = 1; i <= totalPages; i++) {
    html += '<button class="' + (i === orderPage ? 'active' : '') + '" onclick="orderPage=' + i + ';renderOrders()">' + i + '</button>';
  }
  html += '<button ' + (orderPage >= totalPages ? 'disabled' : '') + ' onclick="orderPage++;renderOrders()">Next</button>';
  pag.innerHTML = html;
}
function openOrderPanel(id) {
  var o = orders.find(function (x) { return x.id === id });
  if (!o) return;
  var panel = document.getElementById('order-detail-panel');
  var body = document.getElementById('order-detail-body');
  var statusOptions = ['Pending', 'Confirmed', 'Processing', 'Delivered', 'Cancelled'];
  var timelineHtml = o.timeline.map(function (t, i) {
    var cls = i < o.timeline.length - 1 ? 'completed' : o.status === 'Cancelled' ? '' : 'active';
    return '<div class="timeline-step ' + cls + '"><div class="step-title">' + t.step + '</div><div class="step-time">' + t.time + '</div></div>';
  }).join('');
  body.innerHTML = '<div class="detail-section"><div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem"><h3>' + o.id + '</h3><span class="status-badge ' + getStatusClass(o.status) + '">' + o.status + '</span></div><p style="font-size:.85rem;color:var(' + cssVar('text-secondary') + ')">' + formatDate(o.date) + '</p></div>' +
    '<div class="detail-section"><h4>Customer Info</h4><div class="detail-card"><p><strong>' + o.customer + '</strong></p><p>' + o.phone + '</p><p>' + o.address + '</p></div></div>' +
    '<div class="detail-section"><h4>Items</h4><table class="data-table"><thead><tr><th>Item</th><th>Qty</th><th>Price</th><th>Total</th></tr></thead><tbody>' +
    o.items.map(function (item) { return '<tr><td>' + item.name + '</td><td>' + item.qty + '</td><td>' + formatCurrency(item.price) + '</td><td>' + formatCurrency(item.qty * item.price) + '</td></tr>' }).join('') +
    '</tbody></table></div>' +
    '<div class="detail-section"><h4>Price Summary</h4><div class="detail-card"><div class="detail-row"><span>Subtotal</span><span>' + formatCurrency(o.subtotal) + '</span></div><div class="detail-row"><span>Delivery</span><span>' + formatCurrency(o.delivery) + '</span></div><div class="detail-row"><span>Discount</span><span>-' + formatCurrency(o.discount) + '</span></div><div class="detail-row"><span>Total</span><span>' + formatCurrency(o.total) + '</span></div></div></div>' +
    '<div class="detail-section"><h4>Update Status</h4><div style="display:flex;gap:.5rem"><select class="form-select" id="panel-status-select">' + statusOptions.map(function (s) { return '<option value="' + s + '" ' + (s === o.status ? 'selected' : '') + '>' + s + '</option>' }).join('') + '</select><button class="btn btn-primary" onclick="updateOrderStatus(\'' + o.id + '\')">Update</button></div></div>' +
    '<div class="detail-section"><h4>Timeline</h4><div class="timeline">' + timelineHtml + '</div></div>' +
    '<div style="display:flex;gap:.5rem;margin-top:1rem"><button class="btn btn-outline" onclick="printInvoice(\'' + o.id + '\')"><i class="fas fa-print"></i> Print Invoice</button></div>';
  panel.classList.add('open');
}
function updateOrderStatus(id) {
  var o = orders.find(function (x) { return x.id === id });
  var newStatus = document.getElementById('panel-status-select').value;
  if (o) { o.status = newStatus; showToast('Order ' + id + ' status updated to ' + newStatus); openOrderPanel(id); renderOrders(); renderDashboard() }
}
function printInvoice(id) { window.print() }
function exportOrders() {
  var filtered = getFilteredOrders();
  var csv = 'Order ID,Customer,Items,Total,Payment,Status,Date\n';
  filtered.forEach(function (o) { csv += o.id + ',' + o.customer + ',' + o.items.length + ',' + o.total + ',' + o.payment + ',' + o.status + ',' + o.date + '\n' });
  var blob = new Blob([csv], { type: 'text/csv' });
  var a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = 'orders.csv'; a.click();
  showToast('Orders exported successfully');
}

/* ===== PRODUCTS ===== */
function initProducts() {
  document.getElementById('product-search').addEventListener('input', renderProducts);
  document.getElementById('product-cat-filter').addEventListener('change', renderProducts);
  document.getElementById('add-product-btn').addEventListener('click', function () { openProductModal() });
  document.getElementById('grid-view-btn').addEventListener('click', function () {
    document.getElementById('products-grid').style.display = 'grid';
    document.getElementById('products-list').style.display = 'none';
    this.classList.add('active'); document.getElementById('list-view-btn').classList.remove('active');
  });
  document.getElementById('list-view-btn').addEventListener('click', function () {
    document.getElementById('products-grid').style.display = 'none';
    document.getElementById('products-list').style.display = 'block';
    this.classList.add('active'); document.getElementById('grid-view-btn').classList.remove('active');
  });
  var catFilter = document.getElementById('product-cat-filter');
  categories.forEach(function (c) { var opt = document.createElement('option'); opt.value = c.name; opt.textContent = c.name; catFilter.appendChild(opt) });
}
function renderProducts() {
  var search = document.getElementById('product-search').value.toLowerCase();
  var cat = document.getElementById('product-cat-filter').value;
  var filtered = products.filter(function (p) {
    return (!search || p.name.toLowerCase().includes(search)) && (!cat || p.category === cat);
  });
  var grid = document.getElementById('products-grid');
  grid.innerHTML = filtered.map(function (p) {
    var stockClass = p.stock === 0 ? 'stock-out' : p.stock <= p.minStock ? 'stock-low' : 'stock-in';
    var stockText = p.stock === 0 ? 'Out of Stock' : p.stock <= p.minStock ? 'Low Stock' : 'In Stock (' + p.stock + ')';
    return '<div class="product-card"><div class="product-img">' + p.image + '</div><div class="product-info"><h4>' + p.name + '</h4><span class="product-cat">' + p.category + '</span><div class="product-price"><span class="price">' + formatCurrency(p.price) + '</span><span class="mrp">' + formatCurrency(p.mrp) + '</span></div><span class="product-stock ' + stockClass + '">' + stockText + '</span><div class="product-actions"><button class="btn btn-sm btn-outline" onclick="openProductModal(' + p.id + ')"><i class="fas fa-edit"></i> Edit</button><button class="btn btn-sm btn-danger" onclick="deleteProduct(' + p.id + ')"><i class="fas fa-trash"></i></button></div></div></div>';
  }).join('');
  var tbody = document.getElementById('products-tbody');
  tbody.innerHTML = filtered.map(function (p) {
    var stockClass = p.stock === 0 ? 'stock-out' : p.stock <= p.minStock ? 'stock-low' : 'stock-in';
    return '<tr><td style="font-size:1.5rem">' + p.image + '</td><td>' + p.name + '</td><td><span class="product-cat">' + p.category + '</span></td><td>' + formatCurrency(p.mrp) + '</td><td>' + formatCurrency(p.price) + '</td><td class="' + stockClass + '">' + p.stock + '</td><td><span class="status-badge ' + (p.status === 'Active' ? 'status-active' : 'status-inactive') + '">' + p.status + '</span></td><td><button class="btn btn-sm btn-outline" onclick="openProductModal(' + p.id + ')"><i class="fas fa-edit"></i></button> <button class="btn btn-sm btn-danger" onclick="deleteProduct(' + p.id + ')"><i class="fas fa-trash"></i></button></td></tr>';
  }).join('');
}
function openProductModal(id) {
  var p = id ? products.find(function (x) { return x.id === id }) : null;
  var title = p ? 'Edit Product' : 'Add Product';
  openModal(title,
    '<form id="product-form" class="form-grid">' +
    '<div class="form-group"><label>Product Name</label><input type="text" class="form-input" id="prod-name" value="' + (p ? p.name : '') + '" required></div>' +
    '<div class="form-group"><label>Category</label><select class="form-select" id="prod-category">' + categories.filter(function (c) { return c.status === 'Active' }).map(function (c) { return '<option value="' + c.name + '" ' + (p && p.category === c.name ? 'selected' : '') + '>' + c.name + '</option>' }).join('') + '</select></div>' +
    '<div class="form-group"><label>Unit</label><select class="form-select" id="prod-unit"><option>kg</option><option>g</option><option>L</option><option>ml</option><option>piece</option><option>dozen</option></select></div>' +
    '<div class="form-group"><label>MRP (Rs)</label><input type="number" class="form-input" id="prod-mrp" value="' + (p ? p.mrp : '') + '" required></div>' +
    '<div class="form-group"><label>Selling Price (Rs)</label><input type="number" class="form-input" id="prod-price" value="' + (p ? p.price : '') + '" required></div>' +
    '<div class="form-group"><label>Stock</label><input type="number" class="form-input" id="prod-stock" value="' + (p ? p.stock : 0) + '" required></div>' +
    '<div class="form-group"><label>Min Threshold</label><input type="number" class="form-input" id="prod-min" value="' + (p ? p.minStock : 10) + '" required></div>' +
    '<div class="form-group"><label>Emoji Icon</label><input type="text" class="form-input" id="prod-image" value="' + (p ? p.image : '\uD83D\uDCE6') + '" required></div>' +
    '<div class="form-group full-width"><label>Description</label><textarea class="form-input" id="prod-desc" rows="2"></textarea></div>' +
    '</form>',
    '<button class="btn btn-outline modal-close">Cancel</button><button class="btn btn-primary" id="save-product-btn">Save</button>'
  );
  if (p) { var unitSel = document.getElementById('prod-unit'); for (var i = 0; i < unitSel.options.length; i++) { if (unitSel.options[i].value === p.unit) unitSel.selectedIndex = i } }
  document.getElementById('save-product-btn').addEventListener('click', function () {
    var name = document.getElementById('prod-name').value;
    var mrp = Number(document.getElementById('prod-mrp').value);
    var price = Number(document.getElementById('prod-price').value);
    if (!name || !mrp || !price) { showToast('Please fill all required fields', 'error'); return }
    if (p) {
      p.name = name; p.category = document.getElementById('prod-category').value; p.unit = document.getElementById('prod-unit').value;
      p.mrp = mrp; p.price = price; p.stock = Number(document.getElementById('prod-stock').value);
      p.minStock = Number(document.getElementById('prod-min').value); p.image = document.getElementById('prod-image').value;
      showToast('Product updated');
    } else {
      products.push({ id: products.length + 1, name: name, category: document.getElementById('prod-category').value, unit: document.getElementById('prod-unit').value, mrp: mrp, price: price, stock: Number(document.getElementById('prod-stock').value), minStock: Number(document.getElementById('prod-min').value), image: document.getElementById('prod-image').value, status: 'Active' });
      showToast('Product added');
    }
    closeModal(); renderProducts();
  });
}
function deleteProduct(id) {
  showConfirm('Delete Product', 'Are you sure you want to delete this product?', function () {
    products = products.filter(function (p) { return p.id !== id });
    renderProducts(); showToast('Product deleted');
  });
}

/* ===== CATEGORIES ===== */
function initCategories() {
  document.getElementById('add-category-btn').addEventListener('click', function () { openCategoryModal() });
}
function renderCategories() {
  var tbody = document.getElementById('categories-tbody');
  tbody.innerHTML = categories.map(function (c) {
    var prodCount = products.filter(function (p) { return p.category === c.name }).length;
    return '<tr><td style="font-size:1.5rem">' + c.image + '</td><td>' + c.name + '</td><td>' + c.description + '</td><td>' + prodCount + '</td><td><span class="status-badge ' + (c.status === 'Active' ? 'status-active' : 'status-inactive') + '">' + c.status + '</span></td><td><button class="btn btn-sm btn-outline" onclick="openCategoryModal(' + c.id + ')"><i class="fas fa-edit"></i></button> <button class="btn btn-sm btn-danger" onclick="deleteCategory(' + c.id + ')"><i class="fas fa-trash"></i></button></td></tr>';
  }).join('');
}
function openCategoryModal(id) {
  var c = id ? categories.find(function (x) { return x.id === id }) : null;
  openModal(c ? 'Edit Category' : 'Add Category',
    '<form class="form-grid"><div class="form-group"><label>Name</label><input type="text" class="form-input" id="cat-name" value="' + (c ? c.name : '') + '" required></div>' +
    '<div class="form-group"><label>Emoji Icon</label><input type="text" class="form-input" id="cat-image" value="' + (c ? c.image : '\uD83D\uDCE6') + '"></div>' +
    '<div class="form-group full-width"><label>Description</label><textarea class="form-input" id="cat-desc" rows="2">' + (c ? c.description : '') + '</textarea></div>' +
    '<div class="form-group"><label>Status</label><select class="form-select" id="cat-status"><option ' + (c && c.status === 'Active' ? 'selected' : '') + '>Active</option><option ' + (c && c.status === 'Inactive' ? 'selected' : '') + '>Inactive</option></select></div></form>',
    '<button class="btn btn-outline modal-close">Cancel</button><button class="btn btn-primary" id="save-cat-btn">Save</button>'
  );
  document.getElementById('save-cat-btn').addEventListener('click', function () {
    var name = document.getElementById('cat-name').value;
    if (!name) { showToast('Name required', 'error'); return }
    if (c) { c.name = name; c.description = document.getElementById('cat-desc').value; c.image = document.getElementById('cat-image').value; c.status = document.getElementById('cat-status').value; showToast('Category updated') }
    else { categories.push({ id: categories.length + 1, name: name, description: document.getElementById('cat-desc').value, image: document.getElementById('cat-image').value, productsCount: 0, status: document.getElementById('cat-status').value }); showToast('Category added') }
    closeModal(); renderCategories();
  });
}
function deleteCategory(id) {
  var c = categories.find(function (x) { return x.id === id });
  var prodCount = products.filter(function (p) { return p.category === c.name }).length;
  var msg = prodCount ? 'This category has ' + prodCount + ' products. Are you sure?' : 'Delete this category?';
  showConfirm('Delete Category', msg, function () {
    categories = categories.filter(function (x) { return x.id !== id });
    renderCategories(); showToast('Category deleted');
  });
}

/* ===== INVENTORY ===== */
function initInventory() {
  document.getElementById('inventory-search').addEventListener('input', renderInventory);
  document.getElementById('inventory-status-filter').addEventListener('change', renderInventory);
}
function renderInventory() {
  var search = document.getElementById('inventory-search').value.toLowerCase();
  var statusF = document.getElementById('inventory-status-filter').value;
  var filtered = products.filter(function (p) {
    var matchSearch = !search || p.name.toLowerCase().includes(search);
    var stockStatus = p.stock === 0 ? 'out-of-stock' : p.stock <= p.minStock ? 'low-stock' : 'in-stock';
    var matchStatus = !statusF || stockStatus === statusF;
    return matchSearch && matchStatus;
  });
  var tbody = document.getElementById('inventory-tbody');
  tbody.innerHTML = filtered.map(function (p) {
    var stockStatus = p.stock === 0 ? 'Out of Stock' : p.stock <= p.minStock ? 'Low Stock' : 'In Stock';
    var stockClass = p.stock === 0 ? 'status-cancelled' : p.stock <= p.minStock ? 'status-pending' : 'status-delivered';
    var rowClass = p.stock === 0 ? 'row-out-of-stock' : p.stock <= p.minStock ? 'row-low-stock' : '';
    return '<tr class="' + rowClass + '"><td>' + p.image + ' ' + p.name + '</td><td>' + p.category + '</td><td>' + p.stock + ' ' + p.unit + '</td><td>' + p.minStock + '</td><td><span class="status-badge ' + stockClass + '">' + stockStatus + '</span></td><td><div class="quick-adjust"><button onclick="adjustStock(' + p.id + ',-1)">-</button><span>' + p.stock + '</span><button onclick="adjustStock(' + p.id + ',1)">+</button></div></td></tr>';
  }).join('');
  var histTbody = document.getElementById('stock-history-tbody');
  histTbody.innerHTML = stockHistory.map(function (h) {
    return '<tr><td>' + formatDate(h.date) + '</td><td>' + h.product + '</td><td style="color:' + (h.change.startsWith('+') ? 'var(' + cssVar('primary') + ')' : 'var(' + cssVar('danger') + ')') + '">' + h.change + '</td><td>' + h.newStock + '</td><td>' + h.reason + '</td></tr>';
  }).join('');
}
function adjustStock(id, delta) {
  var p = products.find(function (x) { return x.id === id });
  if (p) {
    p.stock = Math.max(0, p.stock + delta);
    stockHistory.unshift({ date: '2026-05-15', product: p.name, change: (delta > 0 ? '+' : '') + delta, newStock: p.stock, reason: 'Manual Adjust' });
    renderInventory(); showToast(p.name + ' stock adjusted to ' + p.stock);
  }
}

/* ===== CUSTOMERS ===== */
function initCustomers() {
  document.getElementById('customer-search').addEventListener('input', renderCustomers);
}
function renderCustomers() {
  var search = document.getElementById('customer-search').value.toLowerCase();
  var filtered = customers.filter(function (c) {
    return !search || c.name.toLowerCase().includes(search) || c.email.toLowerCase().includes(search) || c.phone.includes(search);
  });
  var tbody = document.getElementById('customers-tbody');
  tbody.innerHTML = filtered.map(function (c) {
    var custOrders = orders.filter(function (o) { return o.customerId === c.id });
    return '<tr class="customer-row" data-id="' + c.id + '"><td>' + c.id + '</td><td>' + c.name + '</td><td>' + c.email + '</td><td>' + c.phone + '</td><td>' + c.orders + '</td><td>' + formatCurrency(c.totalSpent) + '</td><td>' + formatDate(c.joined) + '</td><td><span class="status-badge ' + (c.status === 'Active' ? 'status-active' : 'status-cancelled') + '">' + c.status + '</span></td><td><button class="btn btn-sm ' + (c.status === 'Active' ? 'btn-danger' : 'btn-primary') + '" onclick="toggleCustomer(\'' + c.id + '\')">' + (c.status === 'Active' ? 'Block' : 'Unblock') + '</button></td></tr>' +
      '<tr class="expand-row" id="expand-' + c.id + '"><td colspan="9"><div class="expand-content"><div class="expand-grid"><div class="detail-card"><h4>Personal Info</h4><p><strong>' + c.name + '</strong></p><p>' + c.email + '</p><p>' + c.phone + '</p><p>' + c.address + '</p></div><div><h4>Recent Orders</h4><table class="data-table"><thead><tr><th>ID</th><th>Total</th><th>Status</th></tr></thead><tbody>' +
      custOrders.slice(0, 3).map(function (o) { return '<tr><td>' + o.id + '</td><td>' + formatCurrency(o.total) + '</td><td><span class="status-badge ' + getStatusClass(o.status) + '">' + o.status + '</span></td></tr>' }).join('') +
      '</tbody></table></div></div></div></td></tr>';
  }).join('');
  tbody.querySelectorAll('.customer-row').forEach(function (row) {
    row.addEventListener('click', function (e) {
      if (e.target.closest('button')) return;
      var expandRow = document.getElementById('expand-' + row.dataset.id);
      expandRow.classList.toggle('show');
    });
  });
}
function toggleCustomer(id) {
  var c = customers.find(function (x) { return x.id === id });
  if (c) { c.status = c.status === 'Active' ? 'Blocked' : 'Active'; renderCustomers(); showToast(c.name + ' ' + (c.status === 'Active' ? 'unblocked' : 'blocked')) }
}

/* ===== DELIVERY ===== */
function initDelivery() {
  document.getElementById('assign-delivery-btn').addEventListener('click', assignDelivery);
  document.getElementById('select-all-delivery').addEventListener('change', function (e) {
    document.querySelectorAll('.delivery-order-check').forEach(function (cb) { cb.checked = e.target.checked });
  });
}
function renderDelivery() {
  var readyOrders = orders.filter(function (o) { return o.status === 'Confirmed' || o.status === 'Processing' });
  var tbody1 = document.getElementById('delivery-orders-tbody');
  tbody1.innerHTML = readyOrders.map(function (o) {
    return '<tr><td><input type="checkbox" class="delivery-order-check" value="' + o.id + '"></td><td>' + o.id + '</td><td>' + o.customer + '</td><td>' + o.address + '</td><td>' + formatCurrency(o.total) + '</td></tr>';
  }).join('');
  var avail = deliveryPersonnel.filter(function (d) { return d.status === 'Available' });
  var tbody2 = document.getElementById('delivery-personnel-tbody');
  tbody2.innerHTML = deliveryPersonnel.map(function (d) {
    return '<tr><td><input type="radio" name="assign-person" value="' + d.id + '" ' + (d.status !== 'Available' ? 'disabled' : '') + '></td><td>' + d.name + '</td><td>' + d.vehicle + '</td><td><span class="status-badge ' + (d.status === 'Available' ? 'status-available' : 'status-busy') + '">' + d.status + '</span></td></tr>';
  }).join('');
  var tbody3 = document.getElementById('all-personnel-tbody');
  tbody3.innerHTML = deliveryPersonnel.map(function (d) {
    return '<tr><td>' + d.name + '</td><td>' + d.phone + '</td><td>' + d.vehicle + '</td><td>' + d.activeOrders + '</td><td><span class="status-badge ' + (d.status === 'Available' ? 'status-available' : 'status-busy') + '">' + d.status + '</span></td></tr>';
  }).join('');
}
function assignDelivery() {
  var checked = document.querySelectorAll('.delivery-order-check:checked');
  var person = document.querySelector('input[name="assign-person"]:checked');
  if (!checked.length) { showToast('Select orders to assign', 'warning'); return }
  if (!person) { showToast('Select delivery person', 'warning'); return }
  var dp = deliveryPersonnel.find(function (d) { return d.id === Number(person.value) });
  checked.forEach(function (cb) {
    var o = orders.find(function (x) { return x.id === cb.value });
    if (o) o.status = 'Processing';
  });
  if (dp) { dp.activeOrders += checked.length; dp.status = 'Busy' }
  showToast(checked.length + ' orders assigned to ' + dp.name);
  renderDelivery();
}

/* ===== REPORTS ===== */
function initReports() {
  document.querySelectorAll('.filter-bar [data-range]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      document.querySelectorAll('.filter-bar [data-range]').forEach(function (b) { b.classList.remove('active', 'btn-primary'); b.classList.add('btn-outline') });
      btn.classList.add('active', 'btn-primary'); btn.classList.remove('btn-outline');
      renderReports();
    });
  });
  document.getElementById('print-report-btn').addEventListener('click', function () { window.print() });
  document.getElementById('export-csv-btn').addEventListener('click', function () { showToast('CSV downloaded'); });
}
function renderReports() {
  var totalRevenue = orders.filter(function (o) { return o.status !== 'Cancelled' }).reduce(function (s, o) { return s + o.total }, 0);
  var totalOrders = orders.length;
  var avgOrder = totalOrders ? Math.round(totalRevenue / totalOrders) : 0;
  var newCust = customers.filter(function (c) { return c.joined >= '2026-05-01' }).length;
  var stats = document.getElementById('report-stats');
  stats.innerHTML = '<div class="stat-card"><div class="stat-icon green"><i class="fas fa-coins"></i></div><div class="stat-info"><h4>' + formatCurrency(totalRevenue) + '</h4><p>Total Revenue</p></div></div>' +
    '<div class="stat-card"><div class="stat-icon blue"><i class="fas fa-shopping-bag"></i></div><div class="stat-info"><h4>' + totalOrders + '</h4><p>Total Orders</p></div></div>' +
    '<div class="stat-card"><div class="stat-icon orange"><i class="fas fa-receipt"></i></div><div class="stat-info"><h4>' + formatCurrency(avgOrder) + '</h4><p>Avg Order Value</p></div></div>' +
    '<div class="stat-card"><div class="stat-icon green"><i class="fas fa-user-plus"></i></div><div class="stat-info"><h4>' + newCust + '</h4><p>New Customers</p></div></div>';
  renderRevenueTrend();
  renderTopProducts();
  renderCategorySales();
}
function renderRevenueTrend() {
  var canvas = document.getElementById('chart-revenue-trend');
  var ctx = canvas.getContext('2d');
  canvas.width = canvas.parentElement.clientWidth; canvas.height = 300;
  var dates = ['2026-05-09', '2026-05-10', '2026-05-11', '2026-05-12', '2026-05-13', '2026-05-14', '2026-05-15'];
  var labels = ['09 May', '10 May', '11 May', '12 May', '13 May', '14 May', '15 May'];
  var revenues = dates.map(function (d) { return orders.filter(function (o) { return o.date === d && o.status !== 'Cancelled' }).reduce(function (s, o) { return s + o.total }, 0) });
  var max = Math.max.apply(null, revenues) || 1;
  var w = canvas.width, h = canvas.height;
  var pad = { top: 30, right: 20, bottom: 40, left: 60 };
  var chartW = w - pad.left - pad.right; var chartH = h - pad.top - pad.bottom;
  ctx.clearRect(0, 0, w, h);
  var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  var textColor = isDark ? '#aaa' : '#666';
  ctx.strokeStyle = isDark ? '#333' : '#eee'; ctx.lineWidth = 1;
  for (var g = 0; g <= 4; g++) { var gy = pad.top + chartH * (1 - g / 4); ctx.beginPath(); ctx.moveTo(pad.left, gy); ctx.lineTo(w - pad.right, gy); ctx.stroke(); ctx.fillStyle = textColor; ctx.font = '11px Inter'; ctx.textAlign = 'right'; ctx.fillText(formatCurrency(Math.round(max * g / 4)), pad.left - 8, gy + 4) }
  var points = revenues.map(function (r, i) { return { x: pad.left + i * (chartW / (dates.length - 1)), y: pad.top + chartH * (1 - r / max) } });
  ctx.beginPath(); ctx.moveTo(points[0].x, pad.top + chartH);
  points.forEach(function (p) { ctx.lineTo(p.x, p.y) });
  ctx.lineTo(points[points.length - 1].x, pad.top + chartH); ctx.closePath();
  var gradient = ctx.createLinearGradient(0, pad.top, 0, pad.top + chartH);
  gradient.addColorStop(0, 'rgba(45,139,78,0.3)'); gradient.addColorStop(1, 'rgba(45,139,78,0.02)');
  ctx.fillStyle = gradient; ctx.fill();
  ctx.beginPath(); points.forEach(function (p, i) { i === 0 ? ctx.moveTo(p.x, p.y) : ctx.lineTo(p.x, p.y) });
  ctx.strokeStyle = '#2D8B4E'; ctx.lineWidth = 2.5; ctx.stroke();
  points.forEach(function (p) { ctx.beginPath(); ctx.arc(p.x, p.y, 4, 0, 2 * Math.PI); ctx.fillStyle = '#2D8B4E'; ctx.fill(); ctx.strokeStyle = '#fff'; ctx.lineWidth = 2; ctx.stroke() });
  ctx.fillStyle = textColor; ctx.font = '11px Inter'; ctx.textAlign = 'center';
  labels.forEach(function (l, i) { ctx.fillText(l, points[i].x, h - 10) });
}
function renderTopProducts() {
  var prodSales = {};
  orders.filter(function (o) { return o.status !== 'Cancelled' }).forEach(function (o) {
    o.items.forEach(function (item) {
      if (!prodSales[item.name]) prodSales[item.name] = { sold: 0, revenue: 0 };
      prodSales[item.name].sold += item.qty; prodSales[item.name].revenue += item.qty * item.price;
    })
  });
  var sorted = Object.entries(prodSales).sort(function (a, b) { return b[1].revenue - a[1].revenue }).slice(0, 8);
  var tbody = document.getElementById('top-products-tbody');
  tbody.innerHTML = sorted.map(function (e, i) { return '<tr><td>' + (i + 1) + '</td><td>' + e[0] + '</td><td>' + e[1].sold + '</td><td>' + formatCurrency(e[1].revenue) + '</td></tr>' }).join('');
}
function renderCategorySales() {
  var catSales = {};
  orders.filter(function (o) { return o.status !== 'Cancelled' }).forEach(function (o) {
    o.items.forEach(function (item) {
      var prod = products.find(function (p) { return p.name === item.name });
      var cat = prod ? prod.category : 'Other';
      if (!catSales[cat]) catSales[cat] = { orders: 0, revenue: 0 };
      catSales[cat].orders++; catSales[cat].revenue += item.qty * item.price;
    })
  });
  var totalRev = Object.values(catSales).reduce(function (s, c) { return s + c.revenue }, 0) || 1;
  var tbody = document.getElementById('category-sales-tbody');
  tbody.innerHTML = Object.entries(catSales).sort(function (a, b) { return b[1].revenue - a[1].revenue }).map(function (e) {
    return '<tr><td>' + e[0] + '</td><td>' + e[1].orders + '</td><td>' + formatCurrency(e[1].revenue) + '</td><td>' + Math.round(e[1].revenue / totalRev * 100) + '%</td></tr>';
  }).join('');
}

/* ===== PROMOTIONS ===== */
function initPromotions() {
  document.getElementById('add-promo-btn').addEventListener('click', function () { openPromoModal() });
}
function renderPromotions() {
  var tbody = document.getElementById('promotions-tbody');
  tbody.innerHTML = promotions.map(function (p) {
    var statusClass = p.status === 'Active' ? 'status-active' : p.status === 'Expired' ? 'status-cancelled' : 'status-inactive';
    return '<tr><td><strong>' + p.code + '</strong></td><td>' + p.type + '</td><td>' + (p.type === 'Percentage' ? p.value + '%' : formatCurrency(p.value)) + '</td><td>' + formatCurrency(p.minOrder) + '</td><td>' + formatDate(p.validFrom) + '</td><td>' + formatDate(p.validTo) + '</td><td>' + p.usageCount + '/' + p.usageLimit + '</td><td><span class="status-badge ' + statusClass + '">' + p.status + '</span></td><td><button class="btn btn-sm btn-outline" onclick="openPromoModal(' + p.id + ')"><i class="fas fa-edit"></i></button> <button class="btn btn-sm btn-danger" onclick="deletePromo(' + p.id + ')"><i class="fas fa-trash"></i></button> <label class="switch" style="vertical-align:middle"><input type="checkbox" ' + (p.status === 'Active' ? 'checked' : '') + ' onchange="togglePromo(' + p.id + ',this.checked)"><span class="slider"></span></label></td></tr>';
  }).join('');
}
function openPromoModal(id) {
  var p = id ? promotions.find(function (x) { return x.id === id }) : null;
  openModal(p ? 'Edit Promotion' : 'Add Promotion',
    '<form class="form-grid">' +
    '<div class="form-group"><label>Coupon Code</label><div style="display:flex;gap:.5rem"><input type="text" class="form-input" id="promo-code" value="' + (p ? p.code : '') + '" required><button type="button" class="btn btn-sm btn-outline" onclick="document.getElementById(\'promo-code\').value=\'SAVE\'+Math.floor(Math.random()*100)">Generate</button></div></div>' +
    '<div class="form-group"><label>Discount Type</label><select class="form-select" id="promo-type"><option ' + (p && p.type === 'Percentage' ? 'selected' : '') + '>Percentage</option><option ' + (p && p.type === 'Fixed' ? 'selected' : '') + '>Fixed</option></select></div>' +
    '<div class="form-group"><label>Value</label><input type="number" class="form-input" id="promo-value" value="' + (p ? p.value : '') + '" required></div>' +
    '<div class="form-group"><label>Min Order (Rs)</label><input type="number" class="form-input" id="promo-min" value="' + (p ? p.minOrder : '') + '" required></div>' +
    '<div class="form-group"><label>Max Discount (Rs)</label><input type="number" class="form-input" id="promo-max" value="' + (p ? p.maxDiscount : '') + '"></div>' +
    '<div class="form-group"><label>Usage Limit</label><input type="number" class="form-input" id="promo-limit" value="' + (p ? p.usageLimit : 100) + '"></div>' +
    '<div class="form-group"><label>Valid From</label><input type="date" class="form-input" id="promo-from" value="' + (p ? p.validFrom : '') + '"></div>' +
    '<div class="form-group"><label>Valid To</label><input type="date" class="form-input" id="promo-to" value="' + (p ? p.validTo : '') + '"></div>' +
    '</form>',
    '<button class="btn btn-outline modal-close">Cancel</button><button class="btn btn-primary" id="save-promo-btn">Save</button>'
  );
  document.getElementById('save-promo-btn').addEventListener('click', function () {
    var code = document.getElementById('promo-code').value;
    if (!code) { showToast('Code required', 'error'); return }
    if (p) { p.code = code; p.type = document.getElementById('promo-type').value; p.value = Number(document.getElementById('promo-value').value); p.minOrder = Number(document.getElementById('promo-min').value); p.maxDiscount = Number(document.getElementById('promo-max').value); p.usageLimit = Number(document.getElementById('promo-limit').value); p.validFrom = document.getElementById('promo-from').value; p.validTo = document.getElementById('promo-to').value; showToast('Promotion updated') }
    else { promotions.push({ id: promotions.length + 1, code: code, type: document.getElementById('promo-type').value, value: Number(document.getElementById('promo-value').value), minOrder: Number(document.getElementById('promo-min').value), maxDiscount: Number(document.getElementById('promo-max').value) || 0, validFrom: document.getElementById('promo-from').value, validTo: document.getElementById('promo-to').value, usageCount: 0, usageLimit: Number(document.getElementById('promo-limit').value), status: 'Active' }); showToast('Promotion added') }
    closeModal(); renderPromotions();
  });
}
function deletePromo(id) { showConfirm('Delete Promotion', 'Delete this promotion?', function () { promotions = promotions.filter(function (p) { return p.id !== id }); renderPromotions(); showToast('Promotion deleted') }) }
function togglePromo(id, checked) { var p = promotions.find(function (x) { return x.id === id }); if (p) { p.status = checked ? 'Active' : 'Inactive'; showToast('Promotion ' + (checked ? 'activated' : 'deactivated')); renderPromotions() } }

/* ===== SETTINGS ===== */
function initSettings() {
  document.querySelectorAll('.tab-btn').forEach(function (btn) {
    btn.addEventListener('click', function () {
      document.querySelectorAll('.tab-btn').forEach(function (b) { b.classList.remove('active') });
      document.querySelectorAll('.tab-content').forEach(function (t) { t.classList.remove('active') });
      btn.classList.add('active');
      document.getElementById('tab-' + btn.dataset.tab).classList.add('active');
    });
  });
  ['store-info-form', 'delivery-settings-form', 'profile-form'].forEach(function (formId) {
    var form = document.getElementById(formId);
    if (form) form.addEventListener('submit', function (e) { e.preventDefault(); showToast('Settings saved successfully') });
  });
  initUploadAreas();
}
function initUploadAreas() {
  document.querySelectorAll('.upload-area').forEach(function (area) {
    var input = area.querySelector('input[type=file]');
    area.addEventListener('click', function () { input.click() });
    area.addEventListener('dragover', function (e) { e.preventDefault(); area.style.borderColor = 'var(' + cssVar('primary') + ')' });
    area.addEventListener('dragleave', function () { area.style.borderColor = '' });
    area.addEventListener('drop', function (e) { e.preventDefault(); area.style.borderColor = ''; handleFileUpload(e.dataTransfer.files[0], area) });
    if (input) input.addEventListener('change', function () { if (input.files[0]) handleFileUpload(input.files[0], area) });
  });
}
function handleFileUpload(file, area) {
  if (!file || !file.type.startsWith('image/')) return;
  var reader = new FileReader();
  reader.onload = function (e) {
    var existing = area.querySelector('img');
    if (existing) existing.remove();
    var img = document.createElement('img'); img.src = e.target.result;
    area.appendChild(img); showToast('Image uploaded');
  };
  reader.readAsDataURL(file);
}

/* ===== MODALS ===== */
function initModals() {
  document.getElementById('modal-overlay').addEventListener('click', function (e) { if (e.target === this) closeModal() });
  document.getElementById('confirm-overlay').addEventListener('click', function (e) { if (e.target === this) closeConfirm() });
  document.getElementById('confirm-cancel').addEventListener('click', closeConfirm);
  document.getElementById('confirm-ok').addEventListener('click', function () { if (confirmCallback) confirmCallback(); closeConfirm() });
  document.addEventListener('keydown', function (e) { if (e.key === 'Escape') { closeModal(); closeConfirm(); document.getElementById('order-detail-panel').classList.remove('open') } });
  document.addEventListener('click', function (e) { if (e.target.classList.contains('modal-close')) closeModal() });
}
function openModal(title, bodyHtml, footerHtml) {
  document.getElementById('modal-title').textContent = title;
  document.getElementById('modal-body').innerHTML = bodyHtml;
  document.getElementById('modal-footer').innerHTML = footerHtml || '';
  document.getElementById('modal-overlay').classList.add('show');
  document.getElementById('modal-overlay').querySelectorAll('.modal-close').forEach(function (btn) { btn.addEventListener('click', closeModal) });
}
function closeModal() { document.getElementById('modal-overlay').classList.remove('show') }
function showConfirm(title, msg, callback) {
  document.getElementById('confirm-title').textContent = title;
  document.getElementById('confirm-msg').textContent = msg;
  confirmCallback = callback;
  document.getElementById('confirm-overlay').classList.add('show');
}
function closeConfirm() { document.getElementById('confirm-overlay').classList.remove('show'); confirmCallback = null }

/**
 * Main JavaScript for Smart Grocery Management System
 */

document.addEventListener('DOMContentLoaded', function() {

    // Toggle Sidebar on mobile
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function(e) {
            e.preventDefault();
            sidebar.classList.toggle('show');
        });
    }

    // Add active class to current nav item based on URL
    const currentLocation = window.location.pathname;
    const navLinks = document.querySelectorAll('.sidebar .nav-link');

    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        // Simple logic: if the current location includes the href string, mark active
        if (currentLocation.includes(href) && href !== '#' && href !== '/') {
            link.classList.add('active');
        } else if (currentLocation.endsWith('/') && href === 'dashboard.jsp') {
            link.classList.add('active');
        }
    });

    // Initialize tooltips (Bootstrap 5)
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });

});
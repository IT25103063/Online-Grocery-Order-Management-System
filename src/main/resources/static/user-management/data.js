// ══════════════════════════════════════
// FreshGrocer — Shared Data & Session
// This simulates what Java backend does
// ══════════════════════════════════════

const FreshGrocer = {

  // ── Save user to localStorage ──
  registerUser(user) {

    const users = this.getAllUsers();

    // Check email exists
    if (users.find(u => u.email === user.email)) {

      return {
        success: false,
        message: "Email already registered."
      };
    }

    user.userId =
      "CUST-" +
      Math.random()
        .toString(36)
        .substring(2,10)
        .toUpperCase();

    user.role   = "customer";
    user.status = "active";

    users.push(user);

    localStorage.setItem(
      "fg_users",
      JSON.stringify(users)
    );

    return { success: true };
  },

  // ── Login check ──
  loginUser(email, password, role) {

    if (role === "admin") {

      const admins = this.getAllAdmins();

      const found = admins.find(
        a =>
          a.email === email &&
          a.password === password
      );

      if (found) {

        localStorage.setItem(
          "fg_session",
          JSON.stringify({
            ...found,
            role: "admin"
          })
        );

        return { success: true };
      }

      return {
        success: false,
        message: "Invalid admin credentials."
      };

    } else {

      const users = this.getAllUsers();

      const found = users.find(
        u =>
          u.email === email &&
          u.password === password &&
          u.status === "active"
      );

      if (found) {

        localStorage.setItem(
          "fg_session",
          JSON.stringify(found)
        );

        return { success: true };
      }

      return {
        success: false,
        message: "Invalid email or password."
      };
    }
  },

  // ── Get logged in user ──
  getSession() {

    const s =
      localStorage.getItem("fg_session");

    return s ? JSON.parse(s) : null;
  },

  // ── Logout ──
  logout() {

    localStorage.removeItem("fg_session");

    window.location.href = "login.html";
  },

  // ── Update profile ──
  updateUser(email, updates) {

    const users = this.getAllUsers();

    const index =
      users.findIndex(
        u => u.email === email
      );

    if (index === -1) return false;

    users[index] = {
      ...users[index],
      ...updates
    };

    localStorage.setItem(
      "fg_users",
      JSON.stringify(users)
    );

    // Update session
    localStorage.setItem(
      "fg_session",
      JSON.stringify(users[index])
    );

    return true;
  },

  // ── Delete user ──
  deleteUser(email) {

    const users = this.getAllUsers();

    const updated =
      users.filter(
        u => u.email !== email
      );

    localStorage.setItem(
      "fg_users",
      JSON.stringify(updated)
    );

    this.logout();
  },

  // ── Reset password ──
  resetPassword(email, newPassword) {

    const users = this.getAllUsers();

    const index =
      users.findIndex(
        u => u.email === email
      );

    if (index === -1) return false;

    users[index].password =
      newPassword;

    localStorage.setItem(
      "fg_users",
      JSON.stringify(users)
    );

    return true;
  },

  // ── Get all users ──
  getAllUsers() {

    const data =
      localStorage.getItem("fg_users");

    if (data) {
      return JSON.parse(data);
    }

    // Default sample users
    const defaults = [

      {
        userId: "CUST-A1B2C3D4",
        username: "John Doe",
        email: "john@example.com",
        password: "password123",
        phone: "0771234567",
        address: "123 Main St, Colombo",
        birthday: "1995-04-28",
        role: "customer",
        status: "active"
      },

      {
        userId: "CUST-E5F6G7H8",
        username: "Mary Silva",
        email: "mary@example.com",
        password: "mary2025",
        phone: "0779876543",
        address: "45 Galle Rd, Colombo",
        birthday: "1998-12-25",
        role: "customer",
        status: "active"
      }

    ];

    localStorage.setItem(
      "fg_users",
      JSON.stringify(defaults)
    );

    return defaults;
  },

  // ── Get all admins ──
  getAllAdmins() {

    return [

      {
        userId: "ADMIN-001",
        username: "Admin User",
        email: "admin@freshgrocer.com",
        password: "admin123",
        role: "admin",
        status: "active"
      }

    ];
  },

  // ── Require login ──
  requireLogin() {

    if (!this.getSession()) {

      window.location.href =
        "login.html";
    }
  }
};
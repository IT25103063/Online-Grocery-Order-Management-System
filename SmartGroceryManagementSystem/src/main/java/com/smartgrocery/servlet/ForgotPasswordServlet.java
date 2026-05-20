package com.smartgrocery.servlet;

import com.smartgrocery.dao.UserDao;
import com.smartgrocery.model.User;
import com.smartgrocery.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Random;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() { userDao = new UserDao(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        if ("find".equals(action)) {
            String email = req.getParameter("email").trim();
            User user = userDao.findAll().stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().trim().equalsIgnoreCase(email))
                    .findFirst().orElse(null);

            if (user == null) {
                req.setAttribute("error", "No account found with that email.");
                req.setAttribute("step", "find");
                req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
                return;
            }

            String otp = String.format("%06d", new Random().nextInt(999999));
            session.setAttribute("fp_otp", otp);
            session.setAttribute("fp_userId", user.getId());
            session.setAttribute("fp_email", email);
            session.setAttribute("fp_expiry", System.currentTimeMillis() + 5 * 60 * 1000);

            try {
                EmailUtil.sendOtp(email, otp);
            } catch (Exception e) {
                // Email failed — still show OTP page for demo
                System.out.println("OTP (email failed): " + otp);
            }
            req.setAttribute("step", "otp");
            req.setAttribute("email", email);
            req.setAttribute("otpHint", otp); // show on screen for demo
            req.getRequestDispatcher("forgot-password.jsp").forward(req, res);

        } else if ("verify".equals(action)) {
            String enteredOtp = req.getParameter("otp").trim();
            String savedOtp   = (String) session.getAttribute("fp_otp");
            Long   expiry     = (Long)   session.getAttribute("fp_expiry");

            if (savedOtp == null || expiry == null || System.currentTimeMillis() > expiry) {
                req.setAttribute("error", "OTP expired. Please try again.");
                req.setAttribute("step", "find");
                req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
                return;
            }
            if (!enteredOtp.equals(savedOtp)) {
                req.setAttribute("error", "Incorrect OTP. Please try again.");
                req.setAttribute("step", "otp");
                req.setAttribute("email", session.getAttribute("fp_email"));
                req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
                return;
            }
            req.setAttribute("step", "reset");
            req.getRequestDispatcher("forgot-password.jsp").forward(req, res);

        } else if ("reset".equals(action)) {
            String userId  = (String) session.getAttribute("fp_userId");
            String newPass = req.getParameter("newPassword");
            String confirm = req.getParameter("confirmPassword");

            if (newPass == null || newPass.length() < 6) {
                req.setAttribute("error", "Password must be at least 6 characters.");
                req.setAttribute("step", "reset");
                req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
                return;
            }
            if (!newPass.equals(confirm)) {
                req.setAttribute("error", "Passwords do not match.");
                req.setAttribute("step", "reset");
                req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
                return;
            }

            User user = userDao.findById(userId);
            if (user != null) {
                user.setPassword(newPass);
                userDao.update(user);
                session.removeAttribute("fp_otp");
                session.removeAttribute("fp_userId");
                session.removeAttribute("fp_expiry");
                res.sendRedirect("login.jsp?msg=PasswordReset");
            } else {
                req.setAttribute("error", "Session expired. Try again.");
                req.setAttribute("step", "find");
                req.getRequestDispatcher("forgot-password.jsp").forward(req, res);
            }
        }
    }
}

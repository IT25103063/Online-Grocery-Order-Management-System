package com.freshgrocer.controller;

import com.freshgrocer.model.CustomerUser;
import com.freshgrocer.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user-management")
public class UserController {

    private UserService userService = new UserService();

    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/user-management/login.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "redirect:/user-management/register.html";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-management/login.html";
        }
        return "redirect:/user-management/profile.html";
    }

    @GetMapping("/forgot")
    public String forgotPage() {
        return "redirect:/user-management/forgot.html";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "") String birthday) {

        CustomerUser customer = new CustomerUser(
                "", username, password, email, "active", phone, address, birthday);

        boolean success = userService.registerCustomer(customer);

        if (success) {
            return "redirect:/user-management/login.html?registered=true";
        } else {
            return "redirect:/user-management/register.html?error=exists";
        }
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "customer") String role,
            HttpSession session) {

        if ("admin".equals(role)) {
            boolean valid = userService.validateAdminLogin(email, password);
            if (valid) {
                session.setAttribute("loggedInUser", email);
                session.setAttribute("role", "admin");
                session.setAttribute("username", "Admin");
                return "redirect:/user-management/profile.html";
            } else {
                return "redirect:/user-management/login.html?error=invalid";
            }
        } else {
            boolean valid = userService.validateCustomerLogin(email, password);
            if (valid) {
                CustomerUser customer = userService.getCustomerByEmail(email);
                session.setAttribute("loggedInUser", email);
                session.setAttribute("role", "customer");
                session.setAttribute("username", customer != null ? customer.getUsername() : email);
                session.setAttribute("customerData", customer);
                if (customer != null && customer.isBirthdayToday()) {
                    session.setAttribute("birthdayDiscount", true);
                }
                return "redirect:/user-management/profile.html";
            } else {
                return "redirect:/user-management/login.html?error=invalid";
            }
        }
    }

    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String phone,
            @RequestParam String address,
            HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-management/login.html";
        }

        String email = (String) session.getAttribute("loggedInUser");
        boolean updated = userService.updateCustomer(email, username, phone, address);

        if (updated) {
            session.setAttribute("username", username);
            CustomerUser customer = userService.getCustomerByEmail(email);
            session.setAttribute("customerData", customer);
        }

        return "redirect:/user-management/profile.html?success=updated";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-management/login.html";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/user-management/profile.html?error=nomatch";
        }

        if (newPassword.length() < 8) {
            return "redirect:/user-management/profile.html?error=tooshort";
        }

        String email = (String) session.getAttribute("loggedInUser");
        boolean changed = userService.changePassword(email, currentPassword, newPassword);

        if (changed) {
            return "redirect:/user-management/profile.html?success=password";
        } else {
            return "redirect:/user-management/profile.html?error=wrongpass";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {

        boolean reset = userService.resetPassword(email, newPassword);

        if (reset) {
            return "redirect:/user-management/login.html?registered=true";
        } else {
            return "redirect:/user-management/forgot.html?error=notfound";
        }
    }

    @PostMapping("/delete-account")
    public String deleteAccount(HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-management/login.html";
        }

        String email = (String) session.getAttribute("loggedInUser");
        userService.deleteCustomer(email);
        session.invalidate();

        return "redirect:/user-management/login.html?deleted=true";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user-management/login.html";
    }

    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmail(@RequestParam String email) {
        try {
            com.freshgrocer.util.UserFileHandler fh = new com.freshgrocer.util.UserFileHandler();
            boolean exists = fh.emailExists(email);
            return "{\"exists\":" + exists + "}";
        } catch (Exception e) {
            return "{\"exists\":false}";
        }
    }

    @GetMapping("/session-info")
    @ResponseBody
    public String sessionInfo(HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        if (email == null) {
            return "{\"loggedIn\":false}";
        }
        CustomerUser cu = (CustomerUser) session.getAttribute("customerData");
        String username = (String) session.getAttribute("username");
        String phone    = cu != null ? cu.getPhoneNumber() : "";
        String address  = cu != null ? cu.getAddress()     : "";
        String userId   = cu != null ? cu.getUserId()      : "";
        String birthday = cu != null ? cu.getBirthday()    : "";

        email    = safe(email);
        username = safe(username);
        phone    = safe(phone);
        address  = safe(address);
        userId   = safe(userId);
        birthday = safe(birthday);

        return "{" +
                "\"loggedIn\":true," +
                "\"email\":\"" + email + "\"," +
                "\"username\":\"" + username + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"address\":\"" + address + "\"," +
                "\"userId\":\"" + userId + "\"," +
                "\"birthday\":\"" + birthday + "\"" +
                "}";
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "'");
    }
}

package com.freshgrocer;

import com.freshgrocer.servlet.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    // ── Login Servlet ────────────────────────────────
    @Bean
    public ServletRegistrationBean<LoginServlet>
    loginServlet() {
        ServletRegistrationBean<LoginServlet> bean =
                new ServletRegistrationBean<>(
                        new LoginServlet(), "/LoginServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Register Servlet ─────────────────────────────
    @Bean
    public ServletRegistrationBean<RegisterServlet>
    registerServlet() {
        ServletRegistrationBean<RegisterServlet> bean =
                new ServletRegistrationBean<>(
                        new RegisterServlet(), "/RegisterServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Update Profile Servlet ───────────────────────
    @Bean
    public ServletRegistrationBean<UpdateProfileServlet>
    updateProfileServlet() {
        ServletRegistrationBean<UpdateProfileServlet> bean =
                new ServletRegistrationBean<>(
                        new UpdateProfileServlet(),
                        "/UpdateProfileServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Delete Account Servlet ───────────────────────
    @Bean
    public ServletRegistrationBean<DeleteAccountServlet>
    deleteAccountServlet() {
        ServletRegistrationBean<DeleteAccountServlet> bean =
                new ServletRegistrationBean<>(
                        new DeleteAccountServlet(),
                        "/DeleteAccountServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Change Password Servlet ──────────────────────
    @Bean
    public ServletRegistrationBean<ChangePasswordServlet>
    changePasswordServlet() {
        ServletRegistrationBean<ChangePasswordServlet> bean =
                new ServletRegistrationBean<>(
                        new ChangePasswordServlet(),
                        "/ChangePasswordServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Reset Password Servlet ───────────────────────
    @Bean
    public ServletRegistrationBean<ResetPasswordServlet>
    resetPasswordServlet() {
        ServletRegistrationBean<ResetPasswordServlet> bean =
                new ServletRegistrationBean<>(
                        new ResetPasswordServlet(),
                        "/ResetPasswordServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Logout Servlet ───────────────────────────────
    @Bean
    public ServletRegistrationBean<LogoutServlet>
    logoutServlet() {
        ServletRegistrationBean<LogoutServlet> bean =
                new ServletRegistrationBean<>(
                        new LogoutServlet(), "/LogoutServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    // ── Check Email Servlet ──────────────────────────
    @Bean
    public ServletRegistrationBean<CheckEmailServlet>
    checkEmailServlet() {
        ServletRegistrationBean<CheckEmailServlet> bean =
                new ServletRegistrationBean<>(
                        new CheckEmailServlet(),
                        "/CheckEmailServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }
}
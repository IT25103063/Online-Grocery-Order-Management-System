package com.groceryhub.service.impl;

import com.groceryhub.dao.AdminDAO;
import com.groceryhub.dto.request.LoginRequest;
import com.groceryhub.dto.response.LoginResponse;
import com.groceryhub.exception.AuthenticationException;
import com.groceryhub.model.Admin;
import com.groceryhub.service.interfaces.IAuthService;
import com.groceryhub.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AdminDAO adminDAO;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new AuthenticationException("Invalid email or password");
        }

        Admin admin = adminDAO.findActiveByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found or inactive"));

        // Update last login
        admin.setLastLogin(LocalDateTime.now());
        adminDAO.save(admin);

        String jwtToken = jwtUtil.generateToken(admin.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(admin.getEmail());

        return LoginResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .user(LoginResponse.UserData.builder()
                        .id(admin.getAdminId())
                        .fullName(admin.getFullName())
                        .email(admin.getEmail())
                        .role(admin.getRole())
                        .build())
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        String userEmail = jwtUtil.extractEmail(refreshToken);
        if (userEmail != null) {
            Admin admin = adminDAO.findActiveByEmail(userEmail)
                    .orElseThrow(() -> new AuthenticationException("User not found or inactive"));

            if (jwtUtil.isTokenValid(refreshToken, admin.getEmail())) {
                String accessToken = jwtUtil.generateToken(admin.getEmail());
                return LoginResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .user(LoginResponse.UserData.builder()
                                .id(admin.getAdminId())
                                .fullName(admin.getFullName())
                                .email(admin.getEmail())
                                .role(admin.getRole())
                                .build())
                        .build();
            }
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    @Override
    public Admin getCurrentAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminDAO.findActiveByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Current user not found"));
    }
}

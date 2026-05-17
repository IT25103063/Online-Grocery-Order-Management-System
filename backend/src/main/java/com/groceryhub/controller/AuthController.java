package com.groceryhub.controller;

import com.groceryhub.dto.request.LoginRequest;
import com.groceryhub.dto.response.ApiResponse;
import com.groceryhub.dto.response.LoginResponse;
import com.groceryhub.model.Admin;
import com.groceryhub.service.interfaces.IAuthService;
import com.groceryhub.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseUtil.success(response, "Login successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Refresh token is required", null);
        }
        LoginResponse response = authService.refreshToken(refreshToken);
        return ResponseUtil.success(response, "Token refreshed successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Admin>> getCurrentAdmin() {
        Admin admin = authService.getCurrentAdmin();
        // Clear password hash before returning
        admin.setPasswordHash(null);
        return ResponseUtil.success(admin);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // In JWT stateless architecture, logout is typically handled client-side by deleting the token.
        // If needed, we could implement a token blacklist here.
        return ResponseUtil.success("Logged out successfully");
    }
}

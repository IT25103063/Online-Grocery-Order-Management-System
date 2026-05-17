package com.groceryhub.service.interfaces;

import com.groceryhub.dto.request.LoginRequest;
import com.groceryhub.dto.response.LoginResponse;
import com.groceryhub.model.Admin;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(String refreshToken);
    Admin getCurrentAdmin();
}

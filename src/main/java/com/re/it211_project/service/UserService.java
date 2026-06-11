package com.re.it211_project.service;
import com.re.it211_project.model.dto.request.ForgotPasswordRequest;
import com.re.it211_project.model.dto.request.RegisterRequest;
import com.re.it211_project.model.dto.request.UpdateUserRequest;
import com.re.it211_project.model.dto.request.UserLogin;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.model.dto.response.RegisterResponse;
import com.re.it211_project.model.dto.response.UserResponse;
import org.springframework.data.domain.Page;


public interface UserService {

    RegisterResponse register(RegisterRequest request);

    JWTResponse login(UserLogin userLogin);
    Page<UserResponse> getAllUsers(int page, int size);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
    void forgotPassword(ForgotPasswordRequest request);
}
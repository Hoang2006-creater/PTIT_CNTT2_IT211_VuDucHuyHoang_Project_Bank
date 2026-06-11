package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.*;
import com.re.it211_project.model.dto.response.ApiDataResponse;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.model.dto.response.RegisterResponse;
import com.re.it211_project.model.dto.response.UserResponse;
import com.re.it211_project.service.RefreshTokenService;
import com.re.it211_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Đăng ký tài khoản thành công",
                        userService.register(request),
                        null,
                        HttpStatus.CREATED
                ),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JWTResponse>> login(
            @Valid  @RequestBody UserLogin request
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Đăng nhập thành công",
                        userService.login(request),
                        null,
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiDataResponse<JWTResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Cấp mới access token thành công",
                        refreshTokenService.refreshToken(request),
                        null,
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>>
    getAllUsers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return ResponseEntity.ok(
                userService.getAllUsers(
                        page,
                        size
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse>
    getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse>
    updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUser(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "Xóa user thành công"
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody LogoutRequest request
    ) {

        refreshTokenService.logout(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(
                "Đăng xuất thành công"
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiDataResponse<Object>>
    forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {

        userService.forgotPassword(request);

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đặt lại mật khẩu thành công",
                        null,
                        null,
                        HttpStatus.OK
                )
        );
    }
}
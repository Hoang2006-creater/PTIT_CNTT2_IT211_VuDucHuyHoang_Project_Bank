package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.*;
import com.re.it211_project.model.dto.response.ApiDataResponse;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.model.dto.response.RegisterResponse;
import com.re.it211_project.model.dto.response.UserResponse;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.RefreshTokenService;
import com.re.it211_project.service.UserService;
import com.re.it211_project.service.impl.TokenBlacklistService;
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
    private final TokenBlacklistService tokenBlacklistService;
    private final JWTProvider jwtProvider;
    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiDataResponse<>(
                                true,
                                "Đăng ký tài khoản thành công",
                                userService.register(request),
                                null,
                                HttpStatus.CREATED
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JWTResponse>> login(
            @Valid @RequestBody UserLogin request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đăng nhập thành công",
                        userService.login(request),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiDataResponse<JWTResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Cấp mới Access Token thành công",
                        refreshTokenService.refreshToken(request),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Lấy danh sách người dùng thành công",
                        userService.getAllUsers(page, size),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Lấy thông tin người dùng thành công",
                        userService.getUserById(id),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Cập nhật người dùng thành công",
                        userService.updateUser(id, request),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>> deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Xóa người dùng thành công",
                        null,
                        null,
                        HttpStatus.OK
                )
        );
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse<Object>> logout(
            @RequestBody LogoutRequest request
    ) {

        // 1. blacklist access token (Redis)
        if (request.getAccessToken() != null) {
            long exp = jwtProvider.getExpiration(request.getAccessToken()).getTime()
                    - System.currentTimeMillis();

            tokenBlacklistService.blacklist(request.getAccessToken(), exp);
        }

        // 2. invalidate refresh token (DB or Redis tùy bạn)
        refreshTokenService.logout(request.getRefreshToken());

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đăng xuất thành công",
                        null,
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiDataResponse<Object>>
    forgotPassword(
            @Valid @RequestBody
            ForgotPasswordRequest request
    ) {

        userService.forgotPassword(request);

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "OTP đã được gửi",
                        null,
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiDataResponse<Object>>
    resetPassword(
            @Valid @RequestBody
            ResetPasswordRequest request
    ) {

        userService.resetPassword(request);

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
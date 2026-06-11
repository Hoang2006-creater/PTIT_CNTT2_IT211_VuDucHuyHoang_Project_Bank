package com.re.it211_project.service.impl;

import com.re.it211_project.model.dto.request.RefreshTokenRequest;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.model.entity.RefreshToken;
import com.re.it211_project.model.entity.User;
import com.re.it211_project.repository.RefreshTokenRepository;
import com.re.it211_project.repository.UserRepository;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Value("${jwt-refresh-expired}")
    private Long jwtRefreshExpired;

    @Override
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(
                        Instant.now()
                                .plusMillis(jwtRefreshExpired)
                )
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void verifyExpiration(
            RefreshToken token
    ) {

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {

            token.setRevoked(true);

            refreshTokenRepository.save(token);

            throw new RuntimeException(
                    "Refresh token đã hết hạn"
            );
        }

        if (Boolean.TRUE.equals(token.getRevoked())) {

            throw new RuntimeException(
                    "Refresh token đã bị thu hồi"
            );
        }

    }

    @Override
    public void logout(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Token không tồn tại"));

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
    @Override
    public JWTResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken oldToken =
                refreshTokenRepository
                        .findByToken(
                                request.getRefreshToken()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Refresh token không tồn tại"
                                )
                        );

        verifyExpiration(oldToken);

        User user = oldToken.getUser();

        // Thu hồi refresh token cũ
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // Tạo refresh token mới
        RefreshToken newRefreshToken =
                createRefreshToken(
                        user.getUsername()
                );

        // Tạo access token mới
        String newAccessToken =
                jwtProvider.generateToken(
                        user.getUsername()
                );

        return JWTResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getIsActive())
                .token(newAccessToken)
                .refreshToken(
                        newRefreshToken.getToken()
                )
                .build();
    }
}
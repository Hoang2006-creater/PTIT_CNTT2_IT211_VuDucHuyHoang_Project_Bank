package com.re.it211_project.service;


import com.re.it211_project.model.dto.request.RefreshTokenRequest;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.model.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    void verifyExpiration(RefreshToken token);
    JWTResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
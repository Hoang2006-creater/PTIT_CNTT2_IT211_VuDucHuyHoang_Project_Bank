package com.re.it211_project.service;

import com.re.it211_project.model.entity.RefreshToken;
import com.re.it211_project.repository.RefreshTokenRepository;
import com.re.it211_project.repository.UserRepository;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    void logout_success() {

        RefreshToken token = new RefreshToken();
        token.setRevoked(false);

        when(refreshTokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        refreshTokenService.logout("abc");

        assertTrue(token.getRevoked());
    }
}
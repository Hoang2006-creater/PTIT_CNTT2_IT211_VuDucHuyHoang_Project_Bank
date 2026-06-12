package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.UserLogin;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.security.jwt.JWTAuthFilter;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.RefreshTokenService;
import com.re.it211_project.service.UserService;
import com.re.it211_project.service.impl.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)

class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;
    // an toàn nếu project có security filter
    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;

    @MockitoBean
    private JWTProvider jwtProvider;

    @Test
    void login_success() throws Exception {

        JWTResponse response = JWTResponse.builder()
                .username("admin")
                .token("jwt-token")
                .refreshToken("refresh-token")
                .build();

        when(userService.login(any(UserLogin.class)))
                .thenReturn(response);

        UserLogin login = new UserLogin();
        login.setUsername("admin");
        login.setPassword("123456");

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(status().isOk());
    }

    @Test
    void register_success() throws Exception {

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "username":"test",
                                  "email":"test@gmail.com",
                                  "password":"123456",
                                  "phoneNumber":"0123456789"
                                }
                                """)
                )
                .andExpect(status().isCreated());
    }
}
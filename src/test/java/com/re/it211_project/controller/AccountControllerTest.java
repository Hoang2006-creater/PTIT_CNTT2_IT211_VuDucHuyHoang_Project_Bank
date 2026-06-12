package com.re.it211_project.controller;


import com.re.it211_project.security.jwt.JWTAuthFilter;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.AccountService;
import com.re.it211_project.service.RefreshTokenService;
import com.re.it211_project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;
    @MockitoBean
    private JWTProvider jwtProvider;

    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;
    @Test
    void get_balance_success() throws Exception {

        when(accountService.getBalance(1L))
                .thenReturn(BigDecimal.valueOf(100000));

        mockMvc.perform(
                        get("/api/v1/accounts/1/balance")
                )
                .andExpect(status().isOk());
    }
}
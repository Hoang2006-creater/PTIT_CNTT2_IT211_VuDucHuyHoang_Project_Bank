package com.re.it211_project.controller;

import com.re.it211_project.security.jwt.JWTAuthFilter;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.RefreshTokenService;
import com.re.it211_project.service.TransactionService;
import com.re.it211_project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TransactionService transactionService;
    @MockitoBean
    private RefreshTokenService refreshTokenService;
    @MockitoBean
    private JWTProvider jwtProvider;
    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;
    @MockitoBean
    private UserService userService;
    // Lấy lịch sử giao dịch
    @Test
    void transaction_history_success() throws Exception {

        when(transactionService.getTransactionHistory(
                anyLong(),
                anyInt(),
                anyInt()
        )).thenReturn(Page.empty());

        mockMvc.perform(
                        get("/api/v1/transactions/history/1")
                )
                .andExpect(status().isOk());
    }
    @Test
    void transfer_success() throws Exception {

        mockMvc.perform(
                        post("/api/v1/transactions/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                    {
                       "fromAccountNumber":"111",
                       "toAccountNumber":"222",
                       "amount":100,
                       "pin":"1234"
                    }
                    """)
                )
                .andExpect(status().isOk());
    }
}

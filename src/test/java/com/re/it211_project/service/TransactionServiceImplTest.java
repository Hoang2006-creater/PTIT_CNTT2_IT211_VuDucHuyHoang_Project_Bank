package com.re.it211_project.service;

import com.re.it211_project.model.dto.request.TransferRequest;
import com.re.it211_project.model.dto.response.TransferResponse;
import com.re.it211_project.model.entity.Account;
import com.re.it211_project.repository.AccountRepository;
import com.re.it211_project.repository.TransactionRepository;
import com.re.it211_project.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void transfer_success() {

        Account from = new Account();
        from.setBalance(BigDecimal.valueOf(1000));
        from.setActive(true);
        from.setTransactionPin("encoded-pin");

        Account to = new Account();
        to.setBalance(BigDecimal.valueOf(500));
        to.setActive(true);

        when(accountRepository.findByAccountNumber("111"))
                .thenReturn(Optional.of(from));

        when(accountRepository.findByAccountNumber("222"))
                .thenReturn(Optional.of(to));

        when(passwordEncoder.matches(
                anyString(),
                anyString()
        )).thenReturn(true);

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("111");
        request.setToAccountNumber("222");
        request.setAmount(BigDecimal.valueOf(100));
        request.setPin("123456");

        TransferResponse response =
                transactionService.transfer(request);

        assertNotNull(response);

        assertEquals(
                BigDecimal.valueOf(900),
                from.getBalance()
        );

        assertEquals(
                BigDecimal.valueOf(600),
                to.getBalance()
        );
    }

    @Test
    void transfer_not_enough_money() {

        Account from = new Account();
        from.setBalance(BigDecimal.ZERO);
        from.setActive(true);
        from.setTransactionPin("encoded-pin");

        Account to = new Account();
        to.setBalance(BigDecimal.valueOf(500));
        to.setActive(true);

        when(accountRepository.findByAccountNumber("111"))
                .thenReturn(Optional.of(from));

        when(accountRepository.findByAccountNumber("222"))
                .thenReturn(Optional.of(to));

        when(passwordEncoder.matches(
                anyString(),
                anyString()
        )).thenReturn(true);

        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("111");
        request.setToAccountNumber("222");
        request.setAmount(BigDecimal.valueOf(100));
        request.setPin("123456");

        assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(request)
        );
    }

}

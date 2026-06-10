package com.re.it211_project.service;

import com.re.it211_project.model.dto.request.UpdateAccountRequest;
import com.re.it211_project.model.dto.response.AccountResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface AccountService {

    Page<AccountResponse> getAllAccounts(int page, int size);

    AccountResponse getAccountById(Long id);

    AccountResponse updateAccount(Long id, UpdateAccountRequest request);

    void deleteAccount(Long id);

    BigDecimal getBalance(Long accountId);

}
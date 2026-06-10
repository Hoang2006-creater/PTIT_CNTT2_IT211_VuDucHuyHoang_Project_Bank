package com.re.it211_project.service.impl;

import com.re.it211_project.model.dto.request.UpdateAccountRequest;
import com.re.it211_project.model.dto.response.AccountResponse;
import com.re.it211_project.model.entity.Account;
import com.re.it211_project.repository.AccountRepository;
import com.re.it211_project.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Page<AccountResponse> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));
        return convertToResponse(account);
    }

    @Override
    public AccountResponse updateAccount(Long id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));

        account.setBalance(request.getBalance());
        account.setActive(request.getActive());
        account.setCurrency(request.getCurrency());
        account.setUpdatedAt(java.time.LocalDateTime.now());

        Account updated = accountRepository.save(account);
        return convertToResponse(updated);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));
        accountRepository.delete(account);
    }

    private AccountResponse convertToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .active(account.getActive())
                .username(account.getUser().getUsername())
                .build();
    }
    @Override
    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        return account.getBalance();
    }

}

package com.re.it211_project.service.impl;

import com.re.it211_project.model.dto.request.TransferRequest;
import com.re.it211_project.model.dto.response.TransactionResponse;
import com.re.it211_project.model.dto.response.TransferResponse;
import com.re.it211_project.model.entity.Account;
import com.re.it211_project.model.entity.Transaction;
import com.re.it211_project.repository.AccountRepository;
import com.re.it211_project.repository.TransactionRepository;
import com.re.it211_project.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl
        implements TransactionService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public TransferResponse transfer(
            TransferRequest request
    ) {

        Account fromAccount =
                accountRepository
                        .findByAccountNumber(
                                request.getFromAccountNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tài khoản nguồn không tồn tại"
                                )
                        );

        Account toAccount =
                accountRepository
                        .findByAccountNumber(
                                request.getToAccountNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tài khoản đích không tồn tại"
                                )
                        );

        if (!fromAccount.getActive()) {
            throw new RuntimeException(
                    "Tài khoản nguồn bị khóa"
            );
        }

        if (!toAccount.getActive()) {
            throw new RuntimeException(
                    "Tài khoản đích bị khóa"
            );
        }

        if (!passwordEncoder.matches(
                request.getPin(),
                fromAccount.getTransactionPin()
        )) {

            throw new RuntimeException(
                    "PIN không chính xác"
            );
        }

        if (fromAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException(
                    "Số dư không đủ"
            );
        }

        if (request.getFromAccountNumber()
                .equals(request.getToAccountNumber())) {

            throw new RuntimeException(
                    "Không thể chuyển tiền cho chính mình"
            );
        }
        fromAccount.setBalance(
                fromAccount.getBalance()
                        .subtract(request.getAmount())
        );

        toAccount.setBalance(
                toAccount.getBalance()
                        .add(request.getAmount())
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction =
                Transaction.builder()
                        .transactionCode(
                                "TXN"
                                        + System.currentTimeMillis()
                        )
                        .amount(request.getAmount())
                        .description(request.getDescription())
                        .status("SUCCESS")
                        .createdAt(LocalDateTime.now())
                        .fromAccount(fromAccount)
                        .toAccount(toAccount)
                        .build();

        transactionRepository.save(transaction);

        return TransferResponse.builder()
                .transactionCode(
                        transaction.getTransactionCode()
                )
                .fromAccount(
                        fromAccount.getAccountNumber()
                )
                .toAccount(
                        toAccount.getAccountNumber()
                )
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
    @Override
    public Page<TransactionResponse> getTransactionHistory(
            Long accountId,
            int page,
            int size
    ) {

        accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy tài khoản"
                        )
                );

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()
                );

        return transactionRepository
                .findByFromAccountIdOrToAccountId(
                        accountId,
                        accountId,
                        pageable
                )
                .map(this::convertToResponse);
    }

    private TransactionResponse convertToResponse(
            Transaction transaction
    ) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionCode(
                        transaction.getTransactionCode()
                )
                .fromAccount(
                        transaction.getFromAccount()
                                .getAccountNumber()
                )
                .toAccount(
                        transaction.getToAccount()
                                .getAccountNumber()
                )
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
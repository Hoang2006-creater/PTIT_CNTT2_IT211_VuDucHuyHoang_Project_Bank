package com.re.it211_project.service;

import com.re.it211_project.model.dto.request.TransferRequest;
import com.re.it211_project.model.dto.response.TransactionResponse;
import com.re.it211_project.model.dto.response.TransferResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransferResponse transfer(TransferRequest request);
    Page<TransactionResponse> getTransactionHistory(
            Long accountId,
            int page,
            int size
    );
}

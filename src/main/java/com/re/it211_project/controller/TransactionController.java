package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.TransferRequest;
import com.re.it211_project.model.dto.response.ApiDataResponse;
import com.re.it211_project.model.dto.response.TransactionResponse;
import com.re.it211_project.model.dto.response.TransferResponse;
import com.re.it211_project.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<ApiDataResponse<TransferResponse>> transfer(
            @Valid @RequestBody TransferRequest request
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Chuyển tiền thành công",
                        transactionService.transfer(request),
                        null,
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }
    @GetMapping("/history/{accountId}")
    public ResponseEntity<
            ApiDataResponse<Page<TransactionResponse>>
            > getTransactionHistory(

            @PathVariable Long accountId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Lấy lịch sử giao dịch thành công",
                        transactionService
                                .getTransactionHistory(
                                        accountId,
                                        page,
                                        size
                                ),
                        null,
                        HttpStatus.OK
                )
        );
    }
}
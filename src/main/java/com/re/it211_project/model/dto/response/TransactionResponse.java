package com.re.it211_project.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private Long id;

    private String transactionCode;

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    private String description;

    private String status;

    private LocalDateTime createdAt;
}
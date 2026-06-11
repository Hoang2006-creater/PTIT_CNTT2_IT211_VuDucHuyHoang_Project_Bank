package com.re.it211_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotBlank(message = "Không được để trống tài khoản nguồn")
    private String fromAccountNumber;

    @NotBlank(message = "Không được để trống tài khoản đích")
    private String toAccountNumber;

    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;

    private String description;

    @NotBlank(message = "PIN không được để trống")
    private String pin;
}
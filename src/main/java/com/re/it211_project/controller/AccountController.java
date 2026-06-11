package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.ChangePinRequest;
import com.re.it211_project.model.dto.request.UpdateAccountRequest;
import com.re.it211_project.model.dto.response.AccountResponse;
import com.re.it211_project.model.dto.response.ApiDataResponse;
import com.re.it211_project.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<AccountResponse>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Lấy danh sách tài khoản thành công",
                        accountService.getAllAccounts(page, size),
                        null,
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<AccountResponse>> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Lấy tài khoản thành công",
                        accountService.getAccountById(id),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<AccountResponse>>
    updateAccount(

            @PathVariable Long id,

            @Valid
            @RequestBody
            UpdateAccountRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Cập nhật tài khoản thành công",
                        accountService.updateAccount(
                                id,
                                request
                        ),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Xóa tài khoản thành công",
                        null,
                        null,
                        HttpStatus.OK
                )
        );
    }
    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Vấn tin số dư thành công",
                        accountService.getBalance(id),
                        null,
                        HttpStatus.OK
                )
        );
    }
    @PutMapping("/{id}/change-pin")
    public ResponseEntity<ApiDataResponse<AccountResponse>>
    changePin(
            @PathVariable Long id,
            @Valid @RequestBody ChangePinRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đổi mã PIN thành công",
                        accountService.changePin(id, request),
                        null,
                        HttpStatus.OK
                )
        );
    }
}
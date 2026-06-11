package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.UpdateUserRequest;
import com.re.it211_project.model.dto.response.ApiDataResponse;
import com.re.it211_project.model.dto.response.UserResponse;
import com.re.it211_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<UserResponse>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Lấy danh sách người dùng thành công",
                        userService.getAllUsers(page, size),
                        null,
                        HttpStatus.OK
                ),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Lấy user thành công",
                        userService.getUserById(id),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody
            UpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Cập nhật người dùng thành công",
                        userService.updateUser(
                                id,
                                request
                        ),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>>
    deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Xóa user thành công",
                        null,
                        null,
                        HttpStatus.OK
                )
        );
    }
}
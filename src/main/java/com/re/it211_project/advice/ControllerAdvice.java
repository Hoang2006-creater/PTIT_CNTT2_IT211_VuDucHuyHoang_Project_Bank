package com.re.it211_project.advice;

import com.re.it211_project.exception.DuplicateResourceException;
import com.re.it211_project.model.dto.response.ApiDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiDataResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        log.warn("Lỗi xác thực dữ liệu: {}", errors);

        ApiDataResponse<Void> response = ApiDataResponse.<Void>builder()
                .success(false)
                .message("Lỗi xác thực dữ liệu")
                .data(null)
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Bắt lỗi sai thông tin đăng nhập (Sai username hoặc password)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiDataResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Đăng nhập thất bại: {}", ex.getMessage());

        ApiDataResponse<Void> response = ApiDataResponse.<Void>builder()
                .success(false)
                .message("Đăng nhập thất bại")
                .data(null)
                // In ra câu thông báo lỗi thân thiện cho client trong mảng errors
                .errors(List.of("Tên đăng nhập hoặc mật khẩu không chính xác!"))
                .build();

        // 401 Unauthorized là chuẩn cho lỗi xác thực/đăng nhập sai
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiDataResponse<Void>> handleDuplicateResource(DuplicateResourceException ex) {
        log.warn("Lỗi trùng lặp dữ liệu: {}", ex.getMessage());

        ApiDataResponse<Void> response = ApiDataResponse.<Void>builder()
                .success(false)
                .message("Đăng ký không thành công")
                .data(null)
                .errors(List.of(ex.getMessage())) // Đưa thông báo trùng vào mảng errors
                .build();

        // Trả về mã 409 Conflict đại diện cho việc tài nguyên bị trùng lặp
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiDataResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Lỗi hệ thống (Runtime): ", ex);

        ApiDataResponse<Void> response = ApiDataResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .errors(List.of(ex.getClass().getSimpleName()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
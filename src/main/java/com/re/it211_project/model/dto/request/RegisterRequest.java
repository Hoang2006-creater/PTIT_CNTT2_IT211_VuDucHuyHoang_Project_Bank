package com.re.it211_project.model.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Không được để trống tên")
    private String username;

    @NotBlank(message = "Không được bỏ trống mật khẩu")
    private String password;

    @NotBlank(message = "Không được để trống email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Không được để trống sdt")
    private String phoneNumber;
}
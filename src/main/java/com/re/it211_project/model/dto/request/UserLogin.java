package com.re.it211_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLogin {

    @NotBlank(message = "Không được bỏ trống tên")
    private String username;

    @NotBlank(message = "Không được bỏ trống mật khẩu")
    private String password;
}
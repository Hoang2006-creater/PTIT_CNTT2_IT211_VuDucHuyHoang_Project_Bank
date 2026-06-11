package com.re.it211_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;
}
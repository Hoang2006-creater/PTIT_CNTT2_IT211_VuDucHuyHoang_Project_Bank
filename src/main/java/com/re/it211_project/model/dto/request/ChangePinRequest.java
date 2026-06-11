package com.re.it211_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePinRequest {

    @NotBlank(message = "PIN cũ không được để trống")
    private String oldPin;

    @NotBlank(message = "PIN mới không được để trống")
    private String newPin;
}
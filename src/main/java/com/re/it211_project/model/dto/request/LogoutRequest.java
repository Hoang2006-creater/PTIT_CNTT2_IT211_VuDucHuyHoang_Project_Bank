package com.re.it211_project.model.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
package com.re.it211_project.model.dto.response;

import com.re.it211_project.model.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class KycResponse {

    private Long id;

    private String identityNumber;

    private String fullName;

    private String frontImageUrl;

    private String backImageUrl;

    private Status status;

    private LocalDateTime createdAt;
}
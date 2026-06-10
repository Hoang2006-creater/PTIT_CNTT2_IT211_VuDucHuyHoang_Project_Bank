package com.re.it211_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class KycUploadRequest {

    @NotBlank
    private String idNumber;

    @NotBlank
    private String fullName;

    @NotNull
    private LocalDate dob;

    @NotBlank
    private String sex;

    @NotBlank
    private String address;

    private MultipartFile frontImage;

    private MultipartFile backImage;
}
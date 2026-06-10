package com.re.it211_project.controller;

import com.re.it211_project.model.dto.request.KycUploadRequest;
import com.re.it211_project.model.dto.response.KycResponse;
import com.re.it211_project.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<KycResponse> upload(

            @RequestParam Long userId,

            @ModelAttribute
            KycUploadRequest request
    ) {

        return ResponseEntity.ok(
                kycService.upload(
                        userId,
                        request
                )
        );
    }
}
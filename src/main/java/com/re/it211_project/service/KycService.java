package com.re.it211_project.service;

import com.re.it211_project.model.dto.request.KycUploadRequest;
import com.re.it211_project.model.dto.response.KycResponse;

public interface KycService {

    KycResponse upload(
            Long userId,
            KycUploadRequest request
    );
}
package com.re.it211_project.service.impl;

import com.re.it211_project.model.dto.request.KycUploadRequest;
import com.re.it211_project.model.dto.response.KycResponse;
import com.re.it211_project.model.entity.KycProfile;
import com.re.it211_project.model.entity.Status;
import com.re.it211_project.model.entity.User;
import com.re.it211_project.repository.KycRepository;
import com.re.it211_project.repository.UserRepository;
import com.re.it211_project.service.CloudinaryService;
import com.re.it211_project.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class KycServiceImpl implements KycService {

    private final KycRepository kycRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public KycResponse upload(
            Long userId,
            KycUploadRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy User"));

        if (kycRepository.existsByUserId(userId)) {
            throw new RuntimeException(
                    "Người dùng đã gửi hồ sơ KYC"
            );
        }

        String frontUrl =
                cloudinaryService.uploadFile(
                        request.getFrontImage()
                );

        String backUrl = cloudinaryService.uploadFile(request.getBackImage());
        KycProfile profile = KycProfile.builder()
                .idNumber(request.getIdNumber())
                .fullName(request.getFullName())
                .dob(request.getDob())
                .sex(request.getSex())
                .address(request.getAddress())
                .idCardFrontUrl(frontUrl)
                .idCardBackUrl(backUrl)
                .status(Status.PENDING)
                .user(user)
                .build();

        KycProfile saved =
                kycRepository.save(profile);

        return KycResponse.builder()
                .id(saved.getId())
                .identityNumber(saved.getIdNumber())
                .fullName(saved.getFullName())
                .frontImageUrl(saved.getIdCardFrontUrl())
                .backImageUrl(saved.getIdCardBackUrl())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }
    @Override
    public KycResponse approveKyc(Long kycId) {

        KycProfile profile =
                kycRepository.findById(kycId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy hồ sơ KYC"
                                ));

        if (profile.getStatus() == Status.CONFIRM) {
            throw new RuntimeException(
                    "Hồ sơ đã được duyệt"
            );
        }

        profile.setStatus(Status.CONFIRM);
        profile.setVerifiedAt(LocalDateTime.now());

        User user = profile.getUser();
        user.setIsKyc(true);

        userRepository.save(user);

        KycProfile saved =
                kycRepository.save(profile);

        return convertToResponse(saved);
    }
    @Override
    public KycResponse rejectKyc(Long kycId) {

        KycProfile profile =
                kycRepository.findById(kycId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy hồ sơ KYC"
                                ));

        if (profile.getStatus() == Status.REJECT) {
            throw new RuntimeException(
                    "Hồ sơ đã bị từ chối"
            );
        }

        profile.setStatus(Status.REJECT);
        profile.setVerifiedAt(LocalDateTime.now());

        User user = profile.getUser();
        user.setIsKyc(false);

        userRepository.save(user);

        KycProfile saved =
                kycRepository.save(profile);

        return convertToResponse(saved);
    }


    private KycResponse convertToResponse(KycProfile profile) {
        return KycResponse.builder()
                .id(profile.getId())
                .identityNumber(profile.getIdNumber())
                .fullName(profile.getFullName())
                .frontImageUrl(profile.getIdCardFrontUrl())
                .backImageUrl(profile.getIdCardBackUrl())
                .status(profile.getStatus())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}
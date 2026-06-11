package com.re.it211_project.repository;

import com.re.it211_project.model.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp>
    findByEmailAndOtp(
            String email,
            String otp
    );
}

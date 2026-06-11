package com.re.it211_project.service.impl;

import com.re.it211_project.model.dto.request.*;
import com.re.it211_project.model.dto.response.JWTResponse;
import com.re.it211_project.model.dto.response.RegisterResponse;
import com.re.it211_project.model.dto.response.UserResponse;
import com.re.it211_project.model.entity.Account;
import com.re.it211_project.model.entity.PasswordResetOtp;
import com.re.it211_project.model.entity.Role;
import com.re.it211_project.model.entity.User;
import com.re.it211_project.repository.AccountRepository;
import com.re.it211_project.repository.PasswordResetOtpRepository;
import com.re.it211_project.repository.RoleRepository;
import com.re.it211_project.repository.UserRepository;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.RefreshTokenService;
import com.re.it211_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordResetOtpRepository otpRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() ->
                        new RuntimeException("Role CUSTOMER không tồn tại"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .isActive(true)
                .isKyc(false)
                .createdAt(LocalDateTime.now())
                .role(customerRole)
                .build();

        User savedUser = userRepository.save(user);

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .currency("VND")
                .active(true)
                .createdAt(LocalDateTime.now())
                .user(savedUser)
                .build();

        accountRepository.save(account);

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .accountNumber(account.getAccountNumber())
                .message("Đăng ký thành công")
                .build();
    }

    @Override
    public JWTResponse login(UserLogin userLogin) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userLogin.getUsername(),
                                    userLogin.getPassword()
                            )
                    );

            User user =
                    userRepository
                            .findByUsername(
                                    userLogin.getUsername()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );

            String accessToken =
                    jwtProvider.generateToken(
                            user.getUsername()
                    );

            String refreshToken =
                    refreshTokenService
                            .createRefreshToken(
                                    user.getUsername()
                            )
                            .getToken();

            return JWTResponse.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .enabled(user.getIsActive())
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e) {

            log.error("Sai username hoặc password");

            throw new RuntimeException(
                    "Sai username hoặc password"
            );
        }
    }

    @Override
    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable =
                PageRequest.of(page, size);
        return userRepository
                .findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy user"
                                ));

        return convertToResponse(user);
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request
    ) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy user"
                                ));

        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsActive(request.getIsActive());

        User updated =
                userRepository.save(user);

        return convertToResponse(updated);
    }


    @Override
    public void deleteUser(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy user"
                                ));

        userRepository.delete(user);
    }

    private UserResponse convertToResponse(
            User user
    ) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.getIsActive())
                .isKyc(user.getIsKyc())
                .roleName(
                        user.getRole().getName()
                )
                .build();
    }
    private String generateAccountNumber() {

        Random random = new Random();

        String accountNumber;

        do {

            accountNumber =
                    "10"
                            + (10000000
                            + random.nextInt(90000000));

        } while (
                accountRepository
                        .existsByAccountNumber(
                                accountNumber
                        )
        );

        return accountNumber;
    }
    @Override
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Email không tồn tại"
                        ));

        String otp =
                String.valueOf(
                        100000
                                + new Random()
                                .nextInt(900000)
                );

        PasswordResetOtp resetOtp =
                PasswordResetOtp.builder()
                        .email(user.getEmail())
                        .otp(otp)
                        .expiredAt(
                                LocalDateTime.now()
                                        .plusMinutes(5)
                        )
                        .used(false)
                        .build();

        otpRepository.save(resetOtp);

        System.out.println(
                "OTP của user là: "
                        + otp
        );

    }
    @Override
    public void resetPassword(
            ResetPasswordRequest request
    ) {

        PasswordResetOtp otp =
                otpRepository
                        .findByEmailAndOtp(
                                request.getEmail(),
                                request.getOtp()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "OTP không hợp lệ"
                                ));

        if (otp.getUsed()) {

            throw new RuntimeException(
                    "OTP đã sử dụng"
            );
        }

        if (otp.getExpiredAt()
                .isBefore(
                        LocalDateTime.now()
                )) {

            throw new RuntimeException(
                    "OTP đã hết hạn"
            );
        }

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User không tồn tại"
                                ));

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        otp.setUsed(true);

        otpRepository.save(otp);
    }
}

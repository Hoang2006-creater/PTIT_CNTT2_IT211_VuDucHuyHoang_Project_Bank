package com.re.it211_project.service;

import com.re.it211_project.model.dto.request.RegisterRequest;
import com.re.it211_project.model.dto.response.RegisterResponse;
import com.re.it211_project.model.entity.Account;
import com.re.it211_project.model.entity.Role;
import com.re.it211_project.model.entity.User;
import com.re.it211_project.repository.AccountRepository;
import com.re.it211_project.repository.RoleRepository;
import com.re.it211_project.repository.UserRepository;
import com.re.it211_project.security.jwt.JWTProvider;
import com.re.it211_project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_success() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin");
        request.setEmail("admin@gmail.com");
        request.setPassword("123456");

        Role role = new Role();
        role.setName("CUSTOMER");

        User savedUser = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@gmail.com")
                .build();

        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false);

        when(userRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(roleRepository.findByName("CUSTOMER"))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded-password");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse response =
                userService.register(request);

        assertNotNull(response);
    }

    @Test
    void register_username_exists() {

        when(userRepository.existsByUsername(anyString()))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> userService.register(new RegisterRequest())
        );
    }
}

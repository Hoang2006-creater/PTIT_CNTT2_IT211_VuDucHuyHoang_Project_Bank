package com.re.it211_project.security.principal;

import com.re.it211_project.model.entity.User;
import com.re.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            @NonNull String username
    ) throws UsernameNotFoundException {

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Không tồn tại tài khoản: "
                                                + username
                                )
                        );

        return CustomUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .enabled(user.getIsActive())
                .authorities(
                        mapToGrantedAuthorities(
                                user.getRole()
                        )
                )
                .build();
    }

    private List<? extends GrantedAuthority>
    mapToGrantedAuthorities(
            com.re.it211_project.model.entity.Role role
    ) {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + role.getName()
                )
        );
    }
}
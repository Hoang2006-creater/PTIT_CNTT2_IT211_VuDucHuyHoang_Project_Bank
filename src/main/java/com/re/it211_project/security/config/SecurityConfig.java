package com.re.it211_project.security.config;

import com.re.it211_project.security.jwt.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // Public API
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/kyc/upload"
                        ).permitAll()

                        // Chỉ ADMIN
                        .requestMatchers(
                                "/api/v1/users/**"
                        ).hasRole("ADMIN")

                        // ADMIN hoặc CUSTOMER
                        .requestMatchers(
                                "/api/v1/accounts/**",
                                "/api/v1/transactions/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "CUSTOMER"
                        )
                        .anyRequest()
                        .authenticated()
                )
        .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
package com.re.it211_project.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String phoneNumber;

    private Boolean isActive;

    private Boolean isKyc;

    private String roleName;
}
package com.re.it211_project.model.dto.request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String email;

    private String phoneNumber;

    private Boolean isActive;
}
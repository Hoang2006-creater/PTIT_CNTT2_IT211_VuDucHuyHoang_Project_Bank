package com.re.it211_project.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
public class ApiDataResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private Object errors;

    private HttpStatus status;
}
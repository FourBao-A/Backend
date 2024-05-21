package com.fourbao.bookbao.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserEmailUpdateRequest {
    @NotNull @Email
    private String email;
}

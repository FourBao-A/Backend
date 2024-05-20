package com.fourbao.bookbao.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserLoginRequest {
    @NotNull
    private String id;
    @NotNull private String pw;
    @NotNull @Email
    private String email;
}

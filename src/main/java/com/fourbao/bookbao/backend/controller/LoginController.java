package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.UserLoginRequest;
import com.fourbao.bookbao.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public BaseResponse<String> login(@Valid @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return new BaseResponse<>(false, 400, message);
        }
        try {
            loginService.loginToPortal(userLoginRequest, httpServletRequest, response);
            return new BaseResponse<>("로그인 성공");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}

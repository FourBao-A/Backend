package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.UserLoginRequest;
import com.fourbao.bookbao.backend.service.SessionLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SessionLoginController {
    private final SessionLoginService sessionLoginService;

    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        try {
            sessionLoginService.loginToPortal(userLoginRequest, httpServletRequest);
            return new BaseResponse<>("로그인에 성공하였습니다.");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

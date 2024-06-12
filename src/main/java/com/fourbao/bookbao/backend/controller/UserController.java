package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.UserEmailUpdateRequest;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

//User 마이페이지 기능을 제공하는 Controller

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //User의 MyPage 정보 제공
    @GetMapping("/mypage")
    public BaseResponse<UserMyPageResponse> getMyPage(HttpServletRequest request) {
        try {
            UserMyPageResponse userMyPageResponse = userService.getMyPage(request);
            return new BaseResponse<>(userMyPageResponse);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //User의 이메일 수정
    @PatchMapping("/update-email")
    public BaseResponse<String> updateEmail(HttpServletRequest request, @RequestBody UserEmailUpdateRequest emailUpdateRequest) {
        try {
            userService.updateEmail(request, emailUpdateRequest);
            return new BaseResponse<>("이메일 수정을 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserMyPageResponse getMyPage(HttpSession session) throws BaseException {
        Object objectUser = session.getAttribute("user");
        if (objectUser == null) {
            throw new BaseException(BaseResponseStatus.INVALID_SESSION);
        }

        User user = userRepository.findBySchoolNum(objectUser.toString())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        UserMyPageResponse userMyPageResponse = UserMyPageResponse.builder()
                .name(user.getName())
                .schoolNum(user.getSchoolNum())
                .build();

        return userMyPageResponse;
    }


}

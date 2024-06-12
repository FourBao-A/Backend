package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.UserEmailUpdateRequest;
import com.fourbao.bookbao.backend.dto.request.UserLoginRequest;
import com.fourbao.bookbao.backend.dto.response.UserMyPageHistoriesResponse;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.entity.Book;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.BookRepository;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


import static com.fourbao.bookbao.backend.common.properties.JwtProperties.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final JwtUtils jwtUtils;

    // User의 정보를 가져오는 기능
    public User getUser(HttpServletRequest request) throws BaseException {
        String jwtHeader = request.getHeader(JWT_ACCESS_TOKEN_HEADER_NAME);

        if (jwtHeader == null || !jwtHeader.startsWith(JWT_ACCESS_TOKEN_TYPE)) {
            throw new BaseException(BaseResponseStatus.NO_JWT_TOKEN);
        }

        String jwtToken = jwtHeader.replace(JWT_ACCESS_TOKEN_TYPE, EMPTY_STRING);
        String userSchoolId = jwtUtils.getUserSchoolId(jwtToken);

        return userRepository.findBySchoolId(userSchoolId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));
    }

    // 마이페이지 정보를 가져오는 기능
    public UserMyPageResponse getMyPage(HttpServletRequest request) throws BaseException {
        User user = this.getUser(request);

        log.info("[user name]: {}", user.getName());
        List<Book> notSoldBooks = bookRepository.findByUserIdAndSaleState(user.getId(), Book.SaleState.NOT_SOLD);

        List<UserMyPageHistoriesResponse> histories = notSoldBooks.stream()
                .map(UserMyPageHistoriesResponse::entityToUserMyPageHistoriesResponse)
                .collect(Collectors.toList());

        UserMyPageResponse userMyPageResponse = UserMyPageResponse.builder()
                .name(user.getName())
                .id(user.getSchoolId())
                .histories(histories)
                .build();

        return userMyPageResponse;
    }

    // 이메일을 업데이트 해주는 기능
    public void updateEmail(HttpServletRequest request, UserEmailUpdateRequest emailUpdateRequest) throws BaseException {
        User user = this.getUser(request);

        user.setEmail(emailUpdateRequest.getEmail());
        try {
            userRepository.save(user);
        } catch (BaseException e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }
}

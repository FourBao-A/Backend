package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.UserEmailUpdateRequest;
import com.fourbao.bookbao.backend.dto.response.UserMyPageHistoriesResponse;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.entity.Book;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.BookRepository;
import com.fourbao.bookbao.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public UserMyPageResponse getMyPage(HttpSession session) throws BaseException {
        Object objectUser = session.getAttribute("user");
        if (objectUser == null) {
            throw new BaseException(BaseResponseStatus.INVALID_SESSION);
        }

        User user = userRepository.findBySchoolNum(objectUser.toString())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        List<Book> notSoldBooks = bookRepository.findByUserIdAndSaleState(user.getId(), Book.SaleState.NOT_SOLD);

        List<UserMyPageHistoriesResponse> histories = notSoldBooks.stream()
                .map(UserMyPageHistoriesResponse::entityToUserMyPageHistoriesResponse)
                .collect(Collectors.toList());

        UserMyPageResponse userMyPageResponse = UserMyPageResponse.builder()
                .name(user.getName())
                .id(user.getSchoolNum())
                .histories(histories)
                .build();

        return userMyPageResponse;
    }

    public void updateEmail(HttpSession session, UserEmailUpdateRequest emailUpdateRequest) throws BaseException {
        Object objectUser = session.getAttribute("user");
        if (objectUser == null) {
            throw new BaseException(BaseResponseStatus.INVALID_SESSION);
        }

        User user = userRepository.findBySchoolNum(objectUser.toString())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        user.setEmail(emailUpdateRequest.getEmail());
        try {
            userRepository.save(user);
        } catch (BaseException e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }
}

package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.entity.Book;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.BookRepository;
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
public class BookService
{

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public void saveBook(HttpSession session, EnrollBookRequest enrollBookRequest) throws BaseException
    {
        Object objectUser = session.getAttribute("user");
        if (objectUser == null) {
            throw new BaseException(BaseResponseStatus.INVALID_SESSION);
        }

        User user = userRepository.findBySchoolNum(objectUser.toString())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        log.info("[USER]: {}", user.getName());

        Book book = Book.builder()
                .title(enrollBookRequest.getName())
                .author(enrollBookRequest.getAuthor())
                .publisher(enrollBookRequest.getPublisher())
                .price(enrollBookRequest.getPrice())
                .contactEmail(enrollBookRequest.getEmail())
                .dealWay(enrollBookRequest.getDealWay())
                .dealPlace(enrollBookRequest.getPlace())
                .image(enrollBookRequest.getThumbnail())
                .state(enrollBookRequest.getState())
                .askFor(enrollBookRequest.getAskFor())
                .build();

        user.addBook(book);

        try
        {
            userRepository.save(user);
            bookRepository.save(book);
        } catch (Exception e)
        {
            log.error("책 정보 저장 실패: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }
}
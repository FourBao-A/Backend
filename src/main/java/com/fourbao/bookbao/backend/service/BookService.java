package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.entitiy.BookDTO;
import com.fourbao.bookbao.backend.dto.entitiy.UserDTO;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.dto.request.SearchBookRequest;
import com.fourbao.bookbao.backend.entity.Book;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.BookRepository;
import com.fourbao.bookbao.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        if (objectUser == null)
        {
            throw new BaseException(BaseResponseStatus.INVALID_SESSION);
        }

        User user = userRepository.findBySchoolNum(objectUser.toString())
                .orElseThrow(()->new BaseException(BaseResponseStatus.NON_EXIST_USER));

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

    private BookDTO convertToDTO(Book book)
    {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .price(book.getPrice())
                .contactEmail(book.getContactEmail())
                .dealWay(Book.DealWay.valueOf(book.getDealWay().name()))
                .dealPlace(book.getDealPlace())
                .image(book.getImage())
                .state(book.getState())
                .askFor(book.getAskFor())
                .user(UserDTO.builder()
                        .id(book.getUser().getId())
                        .name(book.getUser().getName())
                        .schoolNum(book.getUser().getSchoolNum())
                        .email(book.getUser().getEmail())
                        .build())
                .build();
    }

    public List<BookDTO> searchBooks(SearchBookRequest searchBookRequest)
    {
        List<Book> books = bookRepository.findByKeyword(searchBookRequest.getSearch());
        if (books.isEmpty())
        {
            return new ArrayList<>();
        }
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
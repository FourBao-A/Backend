package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.entity.Book;
import com.fourbao.bookbao.backend.repository.BookRepository;
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

    public void saveBook(EnrollBookRequest enrollBookRequest) throws BaseException
    {
        Book book = Book.builder()
                .title(enrollBookRequest.getTitle())
                .author(enrollBookRequest.getAuthor())
                .publisher(enrollBookRequest.getPublisher())
                .price(enrollBookRequest.getPrice())
                .deal_way(enrollBookRequest.getDeal_way())
                .deal_place(enrollBookRequest.getDeal_place())
                .state(enrollBookRequest.getState())
                .ask_for(enrollBookRequest.getAsk_for())
                .build();
        try
        {
            bookRepository.save(book);
        } catch (Exception e)
        {
            log.error("책 정보 저장 실패: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }
}

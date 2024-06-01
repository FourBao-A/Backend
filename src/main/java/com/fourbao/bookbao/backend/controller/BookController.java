package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.dto.request.SearchBookRequest;
import com.fourbao.bookbao.backend.dto.request.UpdateBookRequest;
import com.fourbao.bookbao.backend.dto.response.SearchBookResponse;
import com.fourbao.bookbao.backend.service.BookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController
{

    private final BookService bookService;

    @PostMapping("/enroll")
    public BaseResponse<String> enrollBookRequestBaseResponse(HttpSession httpSession, @RequestBody EnrollBookRequest enrollBookRequest)
    {
        try
        {
            bookService.saveBook(httpSession, enrollBookRequest);
            return new BaseResponse<>("도서 등록에 성공하였습니다.");
        } catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @GetMapping("/search")
    public BaseResponse<List<SearchBookResponse>> searchBooks(HttpSession httpSession, @RequestBody SearchBookRequest searchBookRequest)
    {
        try
        {
            List<SearchBookResponse> searchBooks = bookService.searchBooks(httpSession, searchBookRequest);
            return new BaseResponse<>(searchBooks);
        } catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus()); // 예외 상황 처리
        }
    }

    @PatchMapping("/update")
    public BaseResponse<String> updateBookInfo(HttpSession httpSession, @RequestBody UpdateBookRequest updateBookRequest) {
        try {
            bookService.updateBookInfo(httpSession, updateBookRequest);
            return new BaseResponse<>("도서 정보 수정에 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

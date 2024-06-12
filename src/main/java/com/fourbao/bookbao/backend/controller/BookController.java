package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.dto.request.UpdateBookRequest;
import com.fourbao.bookbao.backend.dto.response.BookDetailResponse;
import com.fourbao.bookbao.backend.dto.response.SearchBookResponse;
import com.fourbao.bookbao.backend.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//도서 관리 기능을 제공하는 Controller

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController
{

    private final BookService bookService;

    //도서 등록 요청 처리
    @PostMapping("/enroll")
    public BaseResponse<String> enrollBookRequestBaseResponse(HttpServletRequest request, @RequestBody EnrollBookRequest enrollBookRequest)
    {
        try
        {
            bookService.saveBook(request, enrollBookRequest);
            return new BaseResponse<>("도서 등록에 성공하였습니다.");
        } catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //도서 검색 기능
    @GetMapping("/search")
    public BaseResponse<List<SearchBookResponse>> searchBooks(HttpServletRequest request, @RequestParam String search)
    {
        try
        {
            List<SearchBookResponse> searchBooks = bookService.searchBooks(request, search);
            return new BaseResponse<>(searchBooks);
        } catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus()); // 예외 상황 처리
        }
    }

    //도서 정보 수정 기능
    @PatchMapping("/update")
    public BaseResponse<String> updateBookInfo(HttpServletRequest request, @RequestBody UpdateBookRequest updateBookRequest) {
        try {
            bookService.updateBookInfo(request, updateBookRequest);
            return new BaseResponse<>("도서 정보 수정에 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //도서 상세 정보 조회 기능
    @GetMapping("/detail")
    public BaseResponse<BookDetailResponse> getBookDetail(HttpServletRequest request, @RequestParam Long id)
    {
        try
        {
            BookDetailResponse bookDetailResponse = bookService.getBookDetail(request, id);
            return new BaseResponse<>(bookDetailResponse);
        }catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //도서 삭제 기능
    @DeleteMapping("/delete")
    public BaseResponse<String> deleteBook(HttpServletRequest request, @RequestParam Long id)
    {
        try
        {
            bookService.deleteBook(request, id);
            return new BaseResponse<>("도서 삭제에 성공하였습니다.");
        } catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

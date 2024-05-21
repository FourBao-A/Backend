package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.service.BookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/enroll")
    public BaseResponse<String> enrollBookRequestBaseResponse(HttpSession httpSession, @RequestBody EnrollBookRequest enrollBookRequest)
    {
        try
        {
            bookService.saveBook(httpSession, enrollBookRequest);
            return new BaseResponse<>("도서 등록에 성공하였습니다.");
        }catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

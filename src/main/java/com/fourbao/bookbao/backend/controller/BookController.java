package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.service.BookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public BaseResponse<EnrollBookRequest> enrollBookRequestBaseResponse(HttpSession httpSession, @RequestBody EnrollBookRequest enrollBookRequest)
    {
        try
        {
            return new BaseResponse<>(enrollBookRequest);
        }catch (BaseException e)
        {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

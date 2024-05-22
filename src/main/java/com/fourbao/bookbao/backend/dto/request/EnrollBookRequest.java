package com.fourbao.bookbao.backend.dto.request;

import com.fourbao.bookbao.backend.entity.Book;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class EnrollBookRequest
{
    @NotNull
    private String name;    // 책 이름

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @NotNull
    private int price;

    @Email
    private String email;

    @NotNull
    private Book.DealWay dealWay;       // enum 타입 null 허용하려면 추가로 코드 작성해야함

    private String place;

    private String thumbnail;

    private String state;

    private String askFor;
}

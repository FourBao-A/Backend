package com.fourbao.bookbao.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fourbao.bookbao.backend.entity.Book;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookRequest {
    @NotNull
    private Long id;

    private String name;    // 책 이름

    private String author;

    private String publisher;

    private int price;

    @Email
    private String email;

    private Book.DealWay dealWay;       // enum 타입 null 허용하려면 추가로 코드 작성해야함

    private String place;

    private String thumbnail;

    private String state;

    private String askFor;
}

package com.fourbao.bookbao.backend.dto.response;

import com.fourbao.bookbao.backend.entity.Book;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class BookDetailResponse
{
    private String name;

    private String author;

    private String publisher;

    private int price;

    private String askFor;

    @Email
    private String email;

    private Book.DealWay dealWay;

    private String dealPlace;

    private String thumbnail;

    private String state;
}

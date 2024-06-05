package com.fourbao.bookbao.backend.dto.response;

import com.fourbao.bookbao.backend.entity.Book;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
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

    public static BookDetailResponse entityToBookDetailResponse(Book book)
    {
        return new BookDetailResponse(
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice(),
                book.getAskFor(),
                book.getContactEmail(),
                book.getDealWay(),
                book.getDealPlace(),
                book.getImage(),
                book.getState()
        );
    }
}

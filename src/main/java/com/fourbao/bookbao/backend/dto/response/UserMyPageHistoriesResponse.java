package com.fourbao.bookbao.backend.dto.response;

import com.fourbao.bookbao.backend.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMyPageHistoriesResponse {
    private long id;
    private String name;
    private String author;
    private String publisher;
    private int price;

    public static UserMyPageHistoriesResponse entityToUserMyPageHistoriesResponse(Book book) {
        return new UserMyPageHistoriesResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice()
        );
    }
}

package com.fourbao.bookbao.backend.dto.response;

import com.fourbao.bookbao.backend.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchBookResponse {
    private long id;
    private String name;
    private String author;
    private String publisher;
    private int price;
    private String thumbnail;
    private Book.SaleState sell;

    public static SearchBookResponse entityToSearchBookResponse(Book book) {
        return new SearchBookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice(),
                book.getImage(),
                book.getSaleState()
        );
    }
}

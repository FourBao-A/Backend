package com.fourbao.bookbao.backend.dto.entitiy;

import com.fourbao.bookbao.backend.entity.Book;
import lombok.*;

@Getter
@Builder
public class BookDTO
{
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int price;
    private String contactEmail;
    private Book.DealWay dealWay;
    private String dealPlace;
    private String image;
    private String state;
    private String askFor;
    private UserDTO user;
}
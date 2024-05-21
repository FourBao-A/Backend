package com.fourbao.bookbao.backend.dto.request;

import com.fourbao.bookbao.backend.entity.Book;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class EnrollBookRequest
{
    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @NotNull
    private int price;

    @NotNull
    private Book.Deal_way deal_way;

    @NotNull
    private String image;

    private String deal_place;

    private String state;

    private String ask_for;
}

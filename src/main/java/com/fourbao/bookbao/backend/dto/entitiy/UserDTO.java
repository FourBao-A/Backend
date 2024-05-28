package com.fourbao.bookbao.backend.dto.entitiy;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class UserDTO
{
    private Long id;
    private String name;
    private String schoolNum;
    private String email;
    private List<BookDTO> books;
}
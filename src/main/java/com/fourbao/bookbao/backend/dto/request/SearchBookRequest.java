package com.fourbao.bookbao.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SearchBookRequest
{
    @NotNull
    private String search;
}

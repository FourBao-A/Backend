package com.fourbao.bookbao.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMyPageResponse {
    private String name;
    private String schoolNum;
}

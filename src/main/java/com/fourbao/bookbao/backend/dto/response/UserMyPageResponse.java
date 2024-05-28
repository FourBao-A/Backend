package com.fourbao.bookbao.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserMyPageResponse {
    private String name;
    private String id;
    private List<UserMyPageHistoriesResponse> histories;
}

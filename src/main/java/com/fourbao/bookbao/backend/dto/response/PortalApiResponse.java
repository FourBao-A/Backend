package com.fourbao.bookbao.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PortalApiResponse {
    private String msg;
    private Result result;
    private String version;

    public static class Result {
        private String authenticator;
        @Getter private Body body;
        private String code;
        @JsonProperty("is_auth") @Getter private boolean isAuth;
        @JsonProperty("status_code") private int statusCode;
        private boolean success;
    }

    @Getter
    public static class Body {
        private String major;
        private String name;
    }

}
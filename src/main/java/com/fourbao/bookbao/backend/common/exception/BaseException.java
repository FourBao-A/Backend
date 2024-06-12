package com.fourbao.bookbao.backend.common.exception;

import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;


//BaseException 클래스는 프로젝트 전반에서 발생할 수 있는 예외 상황을 처리하기 위한 기본 클래스입니다.

@Getter
@Setter
public class BaseException extends RuntimeException {

    //status는 예외 상황에 대한 응답 상태 정보를 저장합니다.
    private BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}

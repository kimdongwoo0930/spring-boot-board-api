package com.kdw.boardapi.global.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 예외 처리를 위한 커스텀 예외
 * 예) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND)
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
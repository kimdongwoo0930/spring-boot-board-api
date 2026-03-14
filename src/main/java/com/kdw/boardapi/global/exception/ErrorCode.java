package com.kdw.boardapi.global.exception;

import lombok.Getter;

/**
 * 에러 코드 관리
 * HTTP 상태코드와 메시지를 한곳에서 관리
 */
@Getter
public enum ErrorCode {

    // 공통
    INVALID_INPUT(400, "잘못된 입력입니다."),
    NOT_FOUND(404, "존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다."),

    // 회원
    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(409, "이미 사용중인 이메일입니다."),

    // 게시글
    POST_NOT_FOUND(404, "존재하지 않는 게시글입니다."),

    // 댓글
    COMMENT_NOT_FOUND(404, "존재하지 않는 댓글입니다."),

    // 로그인 실패
    INVALID_PASSWORD(401,"비밀번호가 일치하지 않습니다.");

    private final int status;
    private final String message;


    ErrorCode(int status, String message){
        this.status = status;
        this.message = message;
    }

}

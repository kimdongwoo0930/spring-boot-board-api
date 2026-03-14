package com.kdw.boardapi.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.kdw.boardapi.global.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외처리
 * @RestControllerAdvice 로 모든 Controller 예외를 한곳에서 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        log.error("CustomException : {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    // 입력값 검증 예외 처리 (@Valid 실패시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidException(MethodArgumentNotValidException e) {
        log.error("ValidationException : {}", e.getMessage());
        return ResponseEntity
                .status(400)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT));
    }

    // 나머지 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) throws Exception {
        if (e instanceof NoResourceFoundException) throw e;
        log.error("Exception : {}", e.getMessage());
        return ResponseEntity
                .status(500)
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
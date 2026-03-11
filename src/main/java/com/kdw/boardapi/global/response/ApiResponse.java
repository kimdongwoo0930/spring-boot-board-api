package com.kdw.boardapi.global.response;

import com.kdw.boardapi.global.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모든 API 응답을 통일된 형태로 반환
 *
 * 성공 : ApiResponse.success(data)
 * 실패 : ApiResponse.error(errorCode)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    private ApiResponse(int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "성공", data);
    }

    // 성공 (데이터 없음)
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "성공", null);
    }

    // 실패
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getMessage(), null);
    }
    
}

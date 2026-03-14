package com.kdw.boardapi.global.security.dto;

import lombok.Getter;


@Getter
public class LoginRequest {
    private String email;
    private String password;
}
package com.kdw.boardapi.global.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdw.boardapi.global.response.ApiResponse;
import com.kdw.boardapi.global.security.dto.LoginRequest;
import com.kdw.boardapi.global.security.dto.TokenResponse;
import com.kdw.boardapi.global.security.service.AuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthContorller {
    
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> postMethodName(@RequestBody LoginRequest request) {
        //TODO: process POST request
        return ResponseEntity.status(200).body(ApiResponse.success(authService.login(request)));
        
    }
    
}

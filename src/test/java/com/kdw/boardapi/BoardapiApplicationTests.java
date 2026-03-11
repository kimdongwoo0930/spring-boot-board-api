package com.kdw.boardapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("secret")   // secret 프로필 사용
class BoardapiApplicationTests {
    @Test
    void contextLoads() {
    }
}
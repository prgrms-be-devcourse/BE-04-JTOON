package com.devtoon.jtoon.smtp.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmtpServiceTest {

    @Autowired
    SmtpService smtpService;

    @Test
    void test_Email() {
        smtpService.sendMail("parkseyeon99@naver.com", "123456");
    }
}
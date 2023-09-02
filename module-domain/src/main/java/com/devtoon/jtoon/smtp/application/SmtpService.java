package com.devtoon.jtoon.smtp.application;

import com.devtoon.jtoon.exception.ExceptionCode;
import com.devtoon.jtoon.exception.MemberException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmtpService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String to, String randomNumber) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("JTOON 이메일 인증");
            mimeMessageHelper.setText(randomNumber);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException | MailException e) {
            log.error("이메일 생성 실패", e);
            throw new MemberException(ExceptionCode.MEMBER_MESSAGE_SEND_FAILED);
        }
    }
}

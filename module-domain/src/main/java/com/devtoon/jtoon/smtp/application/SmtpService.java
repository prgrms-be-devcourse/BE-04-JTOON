package com.devtoon.jtoon.smtp.application;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.devtoon.jtoon.exception.ExceptionCode;
import com.devtoon.jtoon.exception.MemberException;
import com.devtoon.jtoon.smtp.entity.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmtpService {

    private final JavaMailSender javaMailSender;

    public void sendMail(Mail mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setTo(mail.getTo());
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getText());
            javaMailSender.send(mimeMessage);

        } catch (MessagingException | MailException e) {
            throw new MemberException(ExceptionCode.MEMBER_MESSAGE_SEND_FAILED);
        }
    }
}

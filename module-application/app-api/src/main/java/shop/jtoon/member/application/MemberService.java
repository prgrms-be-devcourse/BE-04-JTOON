package shop.jtoon.member.application;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import shop.jtoon.application.SmtpService;
import shop.jtoon.entity.Mail;
import shop.jtoon.member.request.SignUpReq;
import shop.jtoon.service.MemberDomainService;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDomainService memberDomainService;
    private final SmtpService smtpService;

    public void signUp(SignUpReq signUpReq) {
        memberDomainService.createMember(signUpReq.toDto());
    }

    public String sendEmailAuthentication(String email) {
        memberDomainService.validateDuplicateEmail(email);
        UUID uuid = UUID.randomUUID();
        String randomUuid = uuid.toString().substring(0, 6);
        Mail mail = Mail.forAuthentication(email, randomUuid);

        smtpService.sendMail(mail);

        return randomUuid;
    }

    // public int getCookieCount() {
    //     return memberDomainService.getCookieCount();
    // }
}

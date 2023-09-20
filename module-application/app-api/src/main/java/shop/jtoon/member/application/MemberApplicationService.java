package shop.jtoon.member.application;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import shop.jtoon.application.SmtpService;
import shop.jtoon.entity.Mail;
import shop.jtoon.member.request.SignUpReq;
import shop.jtoon.security.application.JwtApplicationService;
import shop.jtoon.security.request.LoginReq;
import shop.jtoon.security.service.JwtInternalService;
import shop.jtoon.security.util.TokenCookie;
import shop.jtoon.service.MemberDomainService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberApplicationService {

	private final MemberDomainService memberDomainService;
	private final JwtInternalService jwtInternalService;
	private final JwtApplicationService jwtApplicationService;
	private final SmtpService smtpService;

	@Transactional
	public void signUp(SignUpReq signUpReq) {
		memberDomainService.createMember(signUpReq.toDto());
	}

	@Transactional
	public void loginMember(LoginReq loginReq, HttpServletResponse response) {
		memberDomainService.localLoginMember(loginReq.toDto());
		String accessToken = jwtInternalService.generateAccessToken(loginReq.email());
		String refreshToken = jwtInternalService.generateRefreshToken();
		jwtApplicationService.saveRefreshTokenDb(refreshToken, loginReq.email());

		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, accessToken));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, refreshToken));
	}

	public void sendEmailAuthentication(String email) {
		UUID uuid = UUID.randomUUID();
		String randomUuid = uuid.toString().substring(0, 6);

		smtpService.sendMail(Mail.forAuthentication(email, randomUuid));
	}
}

package shop.jtoon.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.jtoon.member.application.MemberApplicationService;
import shop.jtoon.member.request.SignUpReq;
import shop.jtoon.security.request.LoginReq;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberApplicationService memberApplicationService;

	@PostMapping("/sign-up")
	@ResponseStatus(HttpStatus.CREATED)
	public void signUp(@RequestBody @Valid SignUpReq signUpReq) {
		memberApplicationService.signUp(signUpReq);
	}

	@GetMapping("/email-authorization")
	@ResponseStatus(HttpStatus.CREATED)
	public void authenticateEmail(@RequestParam(value = "email") String email) {
		memberApplicationService.sendEmailAuthentication(email);
	}

	@PostMapping("/local-login")
	public void login(@RequestBody @Valid LoginReq loginReq, HttpServletResponse response) {
		memberApplicationService.loginMember(loginReq, response);
	}
}

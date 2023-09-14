package shop.jtoon.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.member.request.SignUpReq;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/sign-up")
	@ResponseStatus(HttpStatus.CREATED)
	public void signUp(@RequestBody @Valid SignUpReq signUpReq) {
		memberService.signUp(signUpReq);
	}

	@GetMapping("/email-authorization")
	public String authenticateEmail(@RequestParam(value = "email") String email) {
		return memberService.sendEmailAuthentication(email);

	}
}

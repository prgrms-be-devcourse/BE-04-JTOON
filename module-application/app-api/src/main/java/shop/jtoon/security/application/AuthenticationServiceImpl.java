package shop.jtoon.security.application;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.security.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final MemberService memberService;

	@Override
	public Authentication getAuthentication(String claimsEmail) {
		MemberDto memberDto = memberService.findMemberDtoByEmail(claimsEmail);

		return new UsernamePasswordAuthenticationToken(memberDto, BLANK,
			List.of(new SimpleGrantedAuthority(memberDto.role().toString())));
	}
}

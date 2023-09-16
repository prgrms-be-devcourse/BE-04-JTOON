package shop.jtoon.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.CustomUserDetails;
import shop.jtoon.entity.Member;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberDomainService memberDomainService;

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberDomainService.findByEmail(email);

		return new CustomUserDetails(member);
	}
}

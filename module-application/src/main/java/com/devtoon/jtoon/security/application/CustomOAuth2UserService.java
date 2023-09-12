package com.devtoon.jtoon.security.application;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.global.common.MemberThreadLocal;
import com.devtoon.jtoon.member.entity.LoginType;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.security.domain.oauth.CustomOAuth2User;
import com.devtoon.jtoon.security.domain.oauth.OAuthAttributes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oauth2User = delegate.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		LoginType loginType = LoginType.from(registrationId);
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
		Map<String, Object> attributes = oauth2User.getAttributes();
		OAuthAttributes extractedAttributes = OAuthAttributes.of(loginType, userNameAttributeName, attributes);
		Member member = generateMember(extractedAttributes);
		MemberThreadLocal.setMember(member);

		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
			attributes,
			userNameAttributeName
		);
	}

	private Member generateMember(OAuthAttributes extractedAttributes) {
		Member member = memberRepository.findByEmail(extractedAttributes.email())
			.orElse(null);

		if (member == null) {
			return memberRepository.save(extractedAttributes.toEntity());
		}

		if (extractedAttributes.loginType() != member.getLoginType()) {
			throw new RuntimeException("이미 다른 소셜 로그인으로 등록된 회원입니다");
		}

		return member;
	}
}


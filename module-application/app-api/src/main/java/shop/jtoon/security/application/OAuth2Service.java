package shop.jtoon.security.application;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.OAuthAttributes;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.security.service.CustomOAuth2UserService;
import shop.jtoon.type.ErrorStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2Service implements CustomOAuth2UserService {

	private final MemberService memberService;

	@Override
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
		Member member = memberService.generateOrGetSocialMember(extractedAttributes.toSignUpDto());

		if (extractedAttributes.loginType() != member.getLoginType()) {
			throw new DuplicatedException(ErrorStatus.MEMBER_DUPLICATE_SOCIAL_LOGIN);
		}

		member.updateLastLogin();

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
			attributes,
			userNameAttributeName
		);
	}
}

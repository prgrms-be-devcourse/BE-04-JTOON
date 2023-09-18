package shop.jtoon.service;

import static shop.jtoon.type.ErrorStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.MemberCookieRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCookieDomainService {

	private final MemberCookieRepository memberCookieRepository;

	@Transactional
	public void createMemberCookie(CookieItem cookieItem) {
		Member member = null; // TODO: member 조회 기능 추가
		MemberCookie memberCookie = MemberCookie.create(cookieItem.getCount(), member);
		memberCookieRepository.save(memberCookie);
	}

	@Transactional
	public void useCookie(Member member, int cookieCount) {
		MemberCookie memberCookie = getCookieByMember(member);
		memberCookie.decreaseCookieCount(cookieCount);
	}

	private MemberCookie getCookieByMember(Member member) {
		return memberCookieRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(MEMBER_COOKIE_NOT_FOUND));
	}
}

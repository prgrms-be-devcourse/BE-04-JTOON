package shop.jtoon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.MemberCookieRepository;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.type.ErrorStatus;

import static shop.jtoon.type.ErrorStatus.MEMBER_COOKIE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCookieDomainService {

    private final MemberCookieRepository memberCookieRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createMemberCookie(CookieItem cookieItem, MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.email())
            .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_EMAIL_NOT_FOUND));
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

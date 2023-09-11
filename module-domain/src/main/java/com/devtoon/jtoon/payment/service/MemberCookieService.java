package com.devtoon.jtoon.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.error.exception.NotFoundException;
import com.devtoon.jtoon.error.model.ErrorStatus;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.payment.entity.MemberCookie;
import com.devtoon.jtoon.payment.repository.MemberCookieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCookieService {

	private final MemberCookieRepository memberCookieRepository;

	public int getCookieCount(Member member) {
		MemberCookie memberCookie = memberCookieRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(ErrorStatus.COOKIE_MEMBER_NOT_FOUND));

		return memberCookie.getCookieCount();
	}
}

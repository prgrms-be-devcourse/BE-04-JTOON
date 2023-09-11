package com.devtoon.jtoon.payment.application;

import org.springframework.stereotype.Service;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.payment.service.MemberCookieService;
import com.devtoon.jtoon.security.domain.jwt.MemberThreadLocal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CookieService {

	private final MemberCookieService memberCookieService;

	public int getCookieCount() {
		Member member = MemberThreadLocal.getMember();

		return memberCookieService.getCookieCount(member);
	}
}

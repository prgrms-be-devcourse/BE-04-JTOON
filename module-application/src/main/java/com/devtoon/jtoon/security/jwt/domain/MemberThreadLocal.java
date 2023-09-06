package com.devtoon.jtoon.security.jwt.domain;

import com.devtoon.jtoon.member.entity.Member;

public final class MemberThreadLocal {

	private static final ThreadLocal<Member> memberThreadLocal;

	static {
		memberThreadLocal = new ThreadLocal<>();
	}

	public static Member getMember() {
		return memberThreadLocal.get();
	}

	public static void setMember(Member member) {
		memberThreadLocal.set(member);
	}

	public static void clear() {
		memberThreadLocal.remove();
	}
}

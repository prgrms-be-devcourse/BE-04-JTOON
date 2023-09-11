package com.devtoon.jtoon.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.payment.entity.MemberCookie;

public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {

	Optional<MemberCookie> findByMember(Member member);
}

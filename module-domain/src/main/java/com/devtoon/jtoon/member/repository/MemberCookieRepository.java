package com.devtoon.jtoon.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.member.entity.MemberCookie;

public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {
}

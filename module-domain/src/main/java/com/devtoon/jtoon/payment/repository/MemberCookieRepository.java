package com.devtoon.jtoon.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.payment.entity.MemberCookie;

public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {
}

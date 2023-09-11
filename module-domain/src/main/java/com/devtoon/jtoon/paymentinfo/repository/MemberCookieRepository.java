package com.devtoon.jtoon.paymentinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.paymentinfo.entity.MemberCookie;

public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {
}

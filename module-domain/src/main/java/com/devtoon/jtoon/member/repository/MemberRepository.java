package com.devtoon.jtoon.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByPhone(String phone);
}

package com.devtoon.jtoon.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.member.entity.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);

	Optional<Member> findByPhone(String phone);

	Optional<Member> findByEmail(String email);
}

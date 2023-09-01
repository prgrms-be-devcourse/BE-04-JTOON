package com.devtoon.jtoon.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

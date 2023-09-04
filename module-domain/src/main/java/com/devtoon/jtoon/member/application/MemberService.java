package com.devtoon.jtoon.member.application;

import org.springframework.stereotype.Service;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.member.request.SignUpDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public void createMember(SignUpDto signUpDto) {
		Member member = signUpDto.toEntity();
		memberRepository.save(member);
	}
}

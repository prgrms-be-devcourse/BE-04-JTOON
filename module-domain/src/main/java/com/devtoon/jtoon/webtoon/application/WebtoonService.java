package com.devtoon.jtoon.webtoon.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.repository.WebtoonRepository;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonService {
	
	private final WebtoonRepository webtoonRepository;
	private final MemberRepository memberRepository;
	
	@Transactional
	public void createWebtoon(Long memberId, CreateWebtoonReq request) {
		validateDuplicateTitle(request.title());
		Member author = getMemberById(memberId);
		Webtoon webtoon = request.toEntity(author);
		webtoonRepository.save(webtoon);
	}

	private Member getMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("member is not found"));
	}

	private void validateDuplicateTitle(String title) {
		if (webtoonRepository.existsByTitle(title)) {
			throw new RuntimeException("이미 존재하는 웹툰 제목입니다.");
		}
	}
}

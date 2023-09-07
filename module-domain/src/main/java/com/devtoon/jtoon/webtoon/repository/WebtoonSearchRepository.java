package com.devtoon.jtoon.webtoon.repository;

import static com.devtoon.jtoon.member.entity.QMember.*;
import static com.devtoon.jtoon.webtoon.entity.QDayOfWeekWebtoon.*;
import static com.devtoon.jtoon.webtoon.entity.QWebtoon.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.devtoon.jtoon.global.util.DynamicQuery;
import com.devtoon.jtoon.webtoon.entity.DayOfWeekWebtoon;
import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WebtoonSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<DayOfWeekWebtoon> findWebtoons(DayOfWeek dayOfWeek, String keyword) {
		return jpaQueryFactory.selectFrom(dayOfWeekWebtoon)
			.innerJoin(dayOfWeekWebtoon.webtoon, webtoon)
			.innerJoin(webtoon.author, member)
			.where(
				DynamicQuery.generateEq(dayOfWeek, dayOfWeekWebtoon.dayOfWeek::eq),
				DynamicQuery.generateEq(keyword, this::containsKeyword)
			)
			.fetch();
	}

	private BooleanExpression containsKeyword(String keyword) {
		return webtoon.title.contains(keyword)
			.or(webtoon.author.nickname.contains(keyword));
	}
}

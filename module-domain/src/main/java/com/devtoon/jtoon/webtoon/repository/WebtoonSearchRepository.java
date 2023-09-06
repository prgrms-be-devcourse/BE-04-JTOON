package com.devtoon.jtoon.webtoon.repository;

import static com.devtoon.jtoon.member.entity.QMember.*;
import static com.devtoon.jtoon.webtoon.entity.QWebtoon.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.devtoon.jtoon.global.util.DynamicQuery;
import com.devtoon.jtoon.webtoon.entity.DayOfWeek;
import com.devtoon.jtoon.webtoon.entity.QWebtoon;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WebtoonSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<Webtoon> findWebtoons(DayOfWeek dayOfWeek, String keyword, Pageable pageable) {
		return jpaQueryFactory.selectFrom(webtoon)
			.innerJoin(webtoon.author, member)
			.where(
				DynamicQuery.generateEq(dayOfWeek, webtoon.dayOfWeeks::contains),
				DynamicQuery.generateEq(keyword, this::containsKeyword)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	public Long countBy(DayOfWeek dayOfWeek, String keyword) {
		return jpaQueryFactory.select(webtoon.count())
			.from(webtoon)
			.innerJoin(webtoon.author, member)
			.where(
				DynamicQuery.generateEq(dayOfWeek, webtoon.dayOfWeeks::contains),
				DynamicQuery.generateEq(keyword, this::containsKeyword)
			)
			.fetchOne();
	}

	private BooleanExpression containsKeyword(String keyword) {
		return QWebtoon.webtoon.title.contains(keyword)
			.or(QWebtoon.webtoon.author.nickname.contains(keyword));
	}
}

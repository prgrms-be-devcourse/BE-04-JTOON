package shop.jtoon.repository;

import static shop.jtoon.entity.QDayOfWeekWebtoon.*;
import static shop.jtoon.entity.QMember.*;
import static shop.jtoon.entity.QWebtoon.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.DayOfWeekWebtoon;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.util.DynamicQuery;

@Repository
@RequiredArgsConstructor
public class WebtoonSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<DayOfWeekWebtoon> findWebtoons(DayOfWeek dayOfWeek, String keyword) {
		return jpaQueryFactory.selectFrom(dayOfWeekWebtoon)
			.join(dayOfWeekWebtoon.webtoon, webtoon)
			.join(webtoon.author, member)
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

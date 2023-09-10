package com.devtoon.jtoon.webtoon.repository;

import static com.devtoon.jtoon.webtoon.entity.QEpisode.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.devtoon.jtoon.global.util.CustomPageRequest;
import com.devtoon.jtoon.global.util.DynamicQuery;
import com.devtoon.jtoon.webtoon.entity.Episode;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EpisodeSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<Episode> getEpisodes(Long webtoonId, CustomPageRequest request) {
		return jpaQueryFactory.selectFrom(episode)
			.where(DynamicQuery.generateEq(webtoonId, episode.webtoon.id::eq))
			.limit(request.getSize())
			.offset(request.getOffset())
			.orderBy(episode.no.desc())
			.fetch();
	}
}

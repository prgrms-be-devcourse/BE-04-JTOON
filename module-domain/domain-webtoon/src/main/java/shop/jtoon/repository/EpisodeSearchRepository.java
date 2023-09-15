package shop.jtoon.repository;

import static shop.jtoon.entity.QEpisode.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.Episode;
import shop.jtoon.type.CustomPageRequest;
import shop.jtoon.util.DynamicQuery;

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

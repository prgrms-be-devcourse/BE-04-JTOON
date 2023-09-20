package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	boolean existsByWebtoonIdAndNo(Long webtoonId, int no);
}

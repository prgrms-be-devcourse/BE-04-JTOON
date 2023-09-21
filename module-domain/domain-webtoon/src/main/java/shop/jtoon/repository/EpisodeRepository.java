package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Webtoon;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	boolean existsByWebtoonAndNo(Webtoon webtoon, int no);
}

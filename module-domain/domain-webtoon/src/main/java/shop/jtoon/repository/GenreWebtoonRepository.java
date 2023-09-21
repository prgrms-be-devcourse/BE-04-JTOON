package shop.jtoon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.GenreWebtoon;
import shop.jtoon.entity.Webtoon;

public interface GenreWebtoonRepository extends JpaRepository<GenreWebtoon, Long> {

	List<GenreWebtoon> findByWebtoon(Webtoon webtoon);
}

package shop.jtoon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import shop.jtoon.entity.GenreWebtoon;

public interface GenreWebtoonRepository extends JpaRepository<GenreWebtoon, Long> {

	@Query("SELECT g FROM GenreWebtoon g WHERE g.webtoon.id = :webtoonId")
	List<GenreWebtoon> findByWebtoonId(@Param("webtoonId") Long webtoonId);
}

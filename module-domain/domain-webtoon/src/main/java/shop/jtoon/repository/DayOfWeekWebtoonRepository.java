package shop.jtoon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import shop.jtoon.entity.DayOfWeekWebtoon;

public interface DayOfWeekWebtoonRepository extends JpaRepository<DayOfWeekWebtoon, Long> {

	@Query("SELECT d FROM DayOfWeekWebtoon d WHERE d.webtoon.id = :webtoonId")
	List<DayOfWeekWebtoon> findByWebtoonId(@Param("webtoonId") Long webtoonId);
}

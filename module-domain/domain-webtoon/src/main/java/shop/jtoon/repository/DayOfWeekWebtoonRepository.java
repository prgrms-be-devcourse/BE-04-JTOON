package shop.jtoon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.DayOfWeekWebtoon;
import shop.jtoon.entity.Webtoon;

public interface DayOfWeekWebtoonRepository extends JpaRepository<DayOfWeekWebtoon, Long> {

	List<DayOfWeekWebtoon> findByWebtoon(Webtoon webtoon);
}

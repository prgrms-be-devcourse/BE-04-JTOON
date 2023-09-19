package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.Webtoon;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

	boolean existsByTitle(String title);
}

package com.devtoon.jtoon.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.webtoon.entity.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	Episode findByIdAndNo(Long id, int no);
}

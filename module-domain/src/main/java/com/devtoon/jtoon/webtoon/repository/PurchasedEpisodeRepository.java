package com.devtoon.jtoon.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.webtoon.entity.PurchasedEpisode;

public interface PurchasedEpisodeRepository extends JpaRepository<PurchasedEpisode, Long> {
}

package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.jtoon.entity.PurchasedEpisode;

public interface PurchasedEpisodeRepository extends JpaRepository<PurchasedEpisode, Long> {
}

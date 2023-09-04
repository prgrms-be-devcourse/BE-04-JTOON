package com.devtoon.jtoon.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.webtoon.entity.Webtoon;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
}

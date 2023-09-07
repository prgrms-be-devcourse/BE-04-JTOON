package com.devtoon.jtoon.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.webtoon.entity.GenreWebtoon;

public interface GenreWebtoonRepository extends JpaRepository<GenreWebtoon, Long> {
}

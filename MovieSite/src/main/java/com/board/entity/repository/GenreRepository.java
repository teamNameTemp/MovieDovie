package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

}

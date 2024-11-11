package com.board.entity.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long>{

}

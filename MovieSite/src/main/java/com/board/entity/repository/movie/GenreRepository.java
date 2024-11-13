package com.board.entity.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.GenreEntity;
import com.board.entity.movie.MovieEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long>{
	public List<GenreEntity> findByMovieId(MovieEntity entity);
}

package com.board.entity.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{

	public List<MovieEntity> findByTitleContaining(String keyword);
//	public Optional<MovieEntity> findByMovieNm(String movieNm);
//	public MovieEntity findByMovieCd(String movieCd);
}

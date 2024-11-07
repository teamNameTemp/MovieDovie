package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Integer>{
	
	
//	public Optional<MovieEntity> findByMovieNm(String movieNm);
//	public MovieEntity findByMovieCd(String movieCd);
}

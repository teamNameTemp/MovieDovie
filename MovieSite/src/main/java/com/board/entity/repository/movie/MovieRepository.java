package com.board.entity.repository.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{
	
	//게시물 목록 보기...배우이름?
	public Page<MovieEntity> findByTitleContainingOrOriginalTitleContaining(String keyword, String keyword2, Pageable pageable);

	public MovieEntity findByTitle(String title);
//	public Optional<MovieEntity> findByMovieNm(String movieNm);
//	public MovieEntity findByMovieCd(String movieCd);
}

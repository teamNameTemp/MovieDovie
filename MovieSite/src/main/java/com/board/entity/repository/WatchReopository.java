package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.MemberEntity;
import com.board.entity.WatchEntity;
import com.board.entity.movie.MovieEntity;

public interface WatchReopository extends JpaRepository<WatchEntity, Long> {
	public WatchEntity findByEmailAndMovieId(MemberEntity email, MovieEntity movieId);

	public List<WatchEntity> findAllByMovieId(MovieEntity movieId);
}

package com.board.entity.repository.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.entity.movie.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
	@Query("SELECT c FROM company c WHERE c.movieId.id = :movieId")
	List<CompanyEntity> findByMovieId(@Param("movieId") Long movieId);
}

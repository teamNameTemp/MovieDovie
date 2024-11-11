package com.board.entity.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.CastEntity;

public interface CastRepository extends JpaRepository<CastEntity, Long> {

}

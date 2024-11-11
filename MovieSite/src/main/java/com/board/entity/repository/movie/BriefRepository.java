package com.board.entity.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.BriefEntity;

public interface BriefRepository extends JpaRepository<BriefEntity, Long> {

}

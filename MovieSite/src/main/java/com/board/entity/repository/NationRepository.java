package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.NationEntity;

public interface NationRepository extends JpaRepository<NationEntity, Long>{

}

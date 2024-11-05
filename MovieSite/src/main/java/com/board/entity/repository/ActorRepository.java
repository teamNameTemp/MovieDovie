package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.ActorEntity;

public interface ActorRepository extends JpaRepository<ActorEntity, Long> {

}

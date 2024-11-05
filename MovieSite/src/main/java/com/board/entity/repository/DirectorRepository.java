package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.DirectorEntity;

public interface DirectorRepository extends JpaRepository<DirectorEntity, Long>{

}

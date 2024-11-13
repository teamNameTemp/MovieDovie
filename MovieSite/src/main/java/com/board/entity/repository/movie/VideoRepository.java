package com.board.entity.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.VideoEntity;

public interface VideoRepository extends JpaRepository<VideoEntity, Long>{

}

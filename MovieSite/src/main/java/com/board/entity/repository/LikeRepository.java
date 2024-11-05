package com.board.entity.repository;

import com.board.entity.LikeEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

	//select * from jpa_like where seqno = :seqno and email=:email
	public LikeEntity findByMovieseqnoAndEmail(Long movieseqno, String email);

	List<LikeEntity> findByEmail(String email);
    List<LikeEntity> findByMovieseqno(Long movieseqno);

} 
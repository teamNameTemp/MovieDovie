package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.LikeEntity;
import com.board.entity.LikeEntityID;
import com.board.entity.MemberEntity;
import com.board.entity.MovieEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeEntityID> {

	//select * from jpa_like where seqno = :seqno and email=:email
	public LikeEntity findBySeqnoAndEmail(MovieEntity board, MemberEntity member);
}

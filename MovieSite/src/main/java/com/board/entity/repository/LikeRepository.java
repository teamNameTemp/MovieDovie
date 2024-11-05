package com.board.entity.repository;

import com.board.entity.LikeEntity;
import com.board.entity.LikeEntityID;
import com.board.entity.MemberEntity;
import com.board.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeEntityID> {

	//select * from jpa_like where seqno = :seqno and email=:email
	public LikeEntity findBySeqnoAndEmail(MovieEntity board, MemberEntity member);

} 
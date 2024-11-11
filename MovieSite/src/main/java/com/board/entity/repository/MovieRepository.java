package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.entity.MovieEntity;

import jakarta.transaction.Transactional;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{
	
	//게시물 조회수 증가 --> Native SQL 
	@Transactional
	@Modifying //테이블에 DML(insert, update, delete)을 실행 시켜 변화를 주었을 경우 테이블에 반영된 내용을 엔티티 클래스에 반영 
	@Query(value="update jpa_movie set hitno = "
			+ "(select nvl(hitno,0) from jpa_movie where seqno = :seqno) + 1 "
			+ "where seqno = :seqno",nativeQuery = true)
	public void hitno(@Param("seqno") Long seqno);

}

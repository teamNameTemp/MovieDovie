package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.entity.RentEntity;

public interface RentRepository extends JpaRepository<RentEntity, Long>{

	//영화 대여 시작. (+ returndate 및 status 자동 설정)
	@Modifying
	@Query(value = "INSERT INTO Rent (rentdate, returndate, status, email, movieseqno) " +
	               "VALUES (SYSDATE, SYSDATE + 14, " +
	               "CASE WHEN SYSDATE + 14 > SYSDATE THEN 'Y' ELSE 'N' END, :email, :movieseqno)",
	               nativeQuery = true)
	void rentMovie(@Param("email") String email, @Param("movieseqno") Long movieseqno);
	
	
	//대여 상태 자동 변경
	@Modifying
	@Query(value = "UPDATE Rent r SET r.status = " +
	               "CASE WHEN r.returndate > SYSDATE THEN 'Y' " +
	               "ELSE 'N' END " +
	               "WHERE r.email = :email AND r.movieseqno = :movieseqno",
	               nativeQuery = true)
	void updateStatus(@Param("email") String email, @Param("movieseqno") Long movieseqno);
	
}

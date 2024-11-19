package com.board.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.entity.RentEntity;

public interface RentRepository extends JpaRepository<RentEntity, Long>{

	//영화 대여 시작. (+ returndate 및 status 자동 설정)
	@Modifying
	@Query(value = "INSERT INTO tbl_rent (rentdate, returndate, status, email, movieseqno) " +
	               "VALUES (SYSDATE, SYSDATE + 3, " +
	               "CASE WHEN SYSDATE + 3 > SYSDATE THEN 'Y' ELSE 'N' END, :email, :movieseqno)",
	               nativeQuery = true)
	public void rentMovie(@Param("email") String email, @Param("movieseqno") Long movieseqno);
	
	
	//대여 상태 자동 변경
	@Modifying
	@Query(value = "UPDATE tbl_rent r SET r.status = " +
	               "CASE WHEN r.returndate > SYSDATE THEN 'Y' " +
	               "ELSE 'N' END " +
	               "WHERE r.email = :email AND r.movieseqno = :movieseqno",
	               nativeQuery = true)
	public void updateStatus(@Param("email") String email, @Param("movieseqno") Long movieseqno);
	
	
	//관리자 페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
	@Query(value = "SELECT TRUNC(r.rentdate) AS rent_date, COUNT(*) " +
            "FROM tbl_rent r " +
            "WHERE r.rentdate BETWEEN :startDate AND :endDate " +
            "GROUP BY TRUNC(r.rentdate) " +
            "ORDER BY TRUNC(r.rentdate)", nativeQuery = true)
	public List<Object[]> RentCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


	//관리자 페이지 - 통계 : 월별 대여 수 확인
	@Query(value = "SELECT TO_CHAR(r.rentdate, 'YYYY-MM') AS month, "
			+ "COUNT(*) AS count "
			+ "FROM tbl_rent r "
			+ "WHERE EXTRACT(YEAR FROM r.rentdate) = :year "
			+ "GROUP BY TO_CHAR(r.rentdate, 'YYYY-MM')", nativeQuery = true)
	public List<Object[]> RentCountByMonth(@Param("year") int year);
	
	
	//관리자 페이지 - 통계 : 올해 대여 status 수 확인
	@Query(value = "SELECT r.status, COUNT(r) FROM rent r WHERE YEAR(r.rentdate) = YEAR(CURRENT_DATE) GROUP BY r.status")
	public List<Object[]> RentStatus();

	
	//관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	@Query(value = "SELECT r.email, COUNT(r.seqno) AS rentCount " +
            "FROM tbl_rent r " +
            "WHERE EXTRACT(YEAR FROM r.rentdate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "GROUP BY r.email " +
            "ORDER BY rentCount DESC", nativeQuery = true)
	public List<Object[]> findTopRenters();
	
	
}

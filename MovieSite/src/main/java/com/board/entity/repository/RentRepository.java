package com.board.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.entity.MemberEntity;
import com.board.entity.RentEntity;
import com.board.entity.movie.MovieEntity;

public interface RentRepository extends JpaRepository<RentEntity, Long>{

	public List<RentEntity> findAllByEmailAndStatus(MemberEntity member, String status);
	public RentEntity findByEmailAndMovieId(MemberEntity member, MovieEntity movie);
	public RentEntity findByEmailAndMovieIdAndStatus(MemberEntity member, MovieEntity movie,String status);
	//모든 회원 자동 반납 확인
	public List<RentEntity> findAllByReturndateBeforeAndStatus(LocalDateTime returndate, String status);
	
	
	//관리자 페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
	@Query(value = "SELECT TRUNC(r.rentdate) AS rent_date, COUNT(*) " +
            "FROM rent r " +
            "WHERE r.rentdate BETWEEN :startDate AND :endDate " +
            "GROUP BY TRUNC(r.rentdate) " +
            "ORDER BY TRUNC(r.rentdate)", nativeQuery = true)
	public List<Object[]> RentCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


	//관리자 페이지 - 통계 : 월별 대여 수 확인
	@Query(value = "SELECT TO_CHAR(r.rentdate, 'YYYY-MM') AS month, "
			+ "COUNT(*) AS count "
			+ "FROM rent r "
			+ "WHERE EXTRACT(YEAR FROM r.rentdate) = :year "
			+ "GROUP BY TO_CHAR(r.rentdate, 'YYYY-MM')", nativeQuery = true)
	public List<Object[]> RentCountByMonth(@Param("year") int year);
	
	
	//관리자 페이지 - 통계 : 올해 대여 status 수 확인
	@Query(value = "SELECT r.status, COUNT(r) FROM rent r WHERE YEAR(r.rentdate) = YEAR(CURRENT_DATE) GROUP BY r.status")
	public List<Object[]> RentStatus();

	
	//관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	@Query(value = "SELECT r.email, COUNT(r.seqno) AS rentCount " +
            "FROM rent r " +
            "WHERE EXTRACT(YEAR FROM r.rentdate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "GROUP BY r.email " +
            "ORDER BY rentCount DESC", nativeQuery = true)
	public List<Object[]> findTopRenters();
	
	
}

package com.board.entity.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
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
	
	//오늘부터 7일전까지의 대여 현황
	@Query(value = "SELECT r.movie_id AS movieId, COUNT(*) AS count " +
            "FROM rent r " +
            "WHERE r.rentdate BETWEEN :startDate AND :endDate " +
            "GROUP BY r.movie_id " +
            "ORDER BY COUNT(*) DESC", 
     nativeQuery = true)
	List<Map<String, Object>> findByRecentWeekList(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
	
	//관리자 페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
	@Query(value = "SELECT TRUNC(r.rentdate) AS rent_date, COUNT(*)" +
            "FROM rent r " +
            "WHERE r.rentdate BETWEEN :startDate AND :endDate " +
            "GROUP BY TRUNC(r.rentdate)" +
            "ORDER BY TRUNC(r.rentdate), COUNT(*) DESC", 
    nativeQuery = true)
	public List<Object[]> RentCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


	//관리자 페이지 - 통계 : 월별 대여 수 확인
	@Query(value = "SELECT TO_CHAR(r.rentdate, 'YYYY-MM') AS month, "
			+ "COUNT(*) AS count "
			+ "FROM rent r "
			+ "WHERE EXTRACT(YEAR FROM r.rentdate) = :year "
			+ "GROUP BY TO_CHAR(r.rentdate, 'YYYY-MM')", nativeQuery = true)
	public List<Object[]> RentCountByMonth(@Param("year") int year);
	
	
	//관리자 페이지 - 통계 : 월별 매출 확인
	@Query(value = "SELECT TO_CHAR(r.rentdate, 'YYYY-MM') AS month, "
			+ "SUM(r.sales) "
			+ "FROM rent r "
			+ "WHERE EXTRACT(YEAR FROM r.rentdate) = :year "
			+ "GROUP BY TO_CHAR(r.rentdate, 'YYYY-MM')", nativeQuery = true)
	public List<Object[]> SalesByMonth(@Param("year") int year);


	//관리자 페이지 - 통계 : 올해 대여 status 수 확인
	@Query(value = "SELECT r.status, COUNT(r) FROM rent r WHERE YEAR(r.rentdate) = YEAR(CURRENT_DATE) GROUP BY r.status")
	public List<Object[]> RentStatus();


	//관리자 페이지 - 통계 : 올해 대여 TOP 회원 닉네임 확인
	@Query("SELECT m.nickname, COUNT(r.seqno) AS rentCount, SUM(r.sales) AS sales " +
		       "FROM rent r " +
		       "JOIN r.email m " +
		       "WHERE EXTRACT(YEAR FROM r.rentdate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
		       "GROUP BY m.nickname " +
		       "ORDER BY sales DESC")
	public List<Object[]> findTopRenterNickname();
	
	
	@Query("SELECT COALESCE(SUM(r.sales), 0) " +
			"FROM rent r " +
			"WHERE r.email.email = :email")
    Long getTotalSalesByEmail(@Param("email") String email);
	
	//관리자 페이지 - 통계 : 전체 회원의 대여 수 확인
	@Query(value = "SELECT COUNT(r.seqno) FROM rent r")
	public int getTotalRent();
	
	//관리자 페이지 - 통계 : 월별 영화발매일 별 매출 확인
	@Query(value = "SELECT TO_CHAR(r.rentdate, 'YYYY-MM') AS month, "
		    + "SUM(CASE WHEN r.sales = 10000 THEN r.sales ELSE 0 END) AS sales_10000, "
		    + "SUM(CASE WHEN r.sales = 6000 THEN r.sales ELSE 0 END) AS sales_6000, "
		    + "SUM(CASE WHEN r.sales = 5000 THEN r.sales ELSE 0 END) AS sales_5000, "
		    + "SUM(CASE WHEN r.sales = 4000 THEN r.sales ELSE 0 END) AS sales_4000, "
		    + "SUM(CASE WHEN r.sales = 1000 THEN r.sales ELSE 0 END) AS sales_1000 "
		    + "FROM rent r "
		    + "WHERE EXTRACT(YEAR FROM r.rentdate) = :year "
		    + "GROUP BY TO_CHAR(r.rentdate, 'YYYY-MM') "
		    + "ORDER BY month", nativeQuery = true)
	public List<Object[]> SalesByReleaseDateByMonth(@Param("year") int year);

	//관리자 페이지 - 통계 : 분기별 영화발매일 별 매출 확인
	@Query(value = "SELECT TO_CHAR(r.rentdate, 'YYYY-Q') AS quarter, "
		    + "SUM(CASE WHEN r.sales = 10000 THEN r.sales ELSE 0 END) AS sales_10000, "
		    + "SUM(CASE WHEN r.sales = 6000 THEN r.sales ELSE 0 END) AS sales_6000, "
		    + "SUM(CASE WHEN r.sales = 5000 THEN r.sales ELSE 0 END) AS sales_5000, "
		    + "SUM(CASE WHEN r.sales = 4000 THEN r.sales ELSE 0 END) AS sales_4000, "
		    + "SUM(CASE WHEN r.sales = 1000 THEN r.sales ELSE 0 END) AS sales_1000 "
		    + "FROM rent r "
		    + "WHERE EXTRACT(YEAR FROM r.rentdate) = :year "
		    + "GROUP BY TO_CHAR(r.rentdate, 'YYYY-Q') "
		    + "ORDER BY quarter", nativeQuery = true)
	public List<Object[]> SalesByReleaseDateByQuarter(@Param("year") int year);
}

package com.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

public interface RentService {

	public Long getMyAccumulatedPayment(String email);
	public List<Map<String,Object>> getAllMyRent(String email, String status);
	public void updateRent(String email, Long movieId);
	//영화 대여 시작
	public String rentMovie(String email, Long movieId);
	
	//나의 대여 정보
	public Map<String,Object> getMyRentInfo(String email, Long movieId);
	
	//관리자페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
    public List<Object[]> RentCount(LocalDateTime startDate, LocalDateTime endDate);

	//관리자페이지 - 통계 : 매출 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
    public List<Object[]> Sales(LocalDateTime startDate, LocalDateTime endDate);
    
	//관리자페이지 - 통계 : 월별 대여 수 확인
	public List<Object[]> RentCountByMonth(int year);
    
	//관리자페이지 - 통계 : 월별 매출 확인
	public List<Object[]> SalesByMonth(int year);
	
	//관리자 페이지 - 통계 : 올해 대여 status 수 확인
	public List<Object[]> RentStatus();

	//관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	public List<Object[]> findTopRenters();
	
	List<Map<String,Object>> getTopList(int topCount);
	
	//관리자 페이지 - 통계 : 월별 영화발매일 별 대여 건수 확인
	public List<Object[]> RentCountByReleaseDateAndMonth(int year);
	
	//관리자 페이지 - 통계 : 최근 일주일 간 영화발매일 별 대여 건수 확인
	public List<Object[]> getRentCountByReleaseDateLastSevenDays(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
	
	//관리자 페이지 - 통계 : 영화 장르별 매출 현황 YTD 
	public List<Object[]> getYTDSalesByGenre();
	
	//매일 자정 확인하여 대여가 하루 남은 사람에게 알림 보내기
	public void autoNotiBeforeReturn();
}

package com.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RentService {

	public void updateRent(String email, Long movieId);
	//영화 대여 시작
	public String rentMovie(String email, Long movieId);
	
	//나의 대여 정보
	public Map<String,Object> getMyRentInfo(String email, Long movieId);
	
	//관리자페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
    public List<Object[]> RentCount(LocalDateTime startDate, LocalDateTime endDate);
    
	//관리자페이지 - 통계 : 월별 대여 수 확인
	public List<Object[]> RentCountByMonth(int year);
    
	//관리자 페이지 - 통계 : 올해 대여 status 수 확인
	public List<Object[]> RentStatus();

	//관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	public List<Object[]> findTopRenters();
	
}

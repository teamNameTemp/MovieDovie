package com.board.service;

import java.time.LocalDateTime;
import java.util.List;

public interface RentService {

	//영화 대여 시작
	public void rentMovie(String email, Long movieseqno);
	
	//관리자페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
    public List<Object[]> RentCount(LocalDateTime startDate, LocalDateTime endDate);
    
	//관리자페이지 - 통계 : 월별 대여 수 확인
	public List<Object[]> RentCountByMonth(int year);
    
	//관리자페이지 - 통계 : 이번 연도 대여 누적 수 확인
	//public List<Object[]> RentCumulative();
	


}

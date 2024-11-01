package com.board.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.entity.repository.RentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService{

	private final RentRepository rentRepository;
	
	
	//영화 대여 시작
	@Transactional
    @Override
	public void rentMovie(String email, Long movieseqno) {
        rentRepository.rentMovie(email, movieseqno);
        rentRepository.updateStatus(email, movieseqno);
    }
	
	
	// 관리자 페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
	@Override
    public List<Object[]> RentCount(LocalDateTime startDate, LocalDateTime endDate){
    	 return rentRepository.RentCount(startDate, endDate);
    }
	
	
	
	//관리자페이지 - 이번 연도 대여 누적 수 확인
	@Override
	public List<Object[]> RentCumulative() {
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfYear = LocalDateTime.of(now.getYear(), 1, 1, 0, 0);

		// 이번 연도 대여 데이터를 가져오기
	    List<Object[]> results = rentRepository.RentCount(startOfYear, now); // LocalDateTime 변환
	    System.out.println("Results size: " + results.size());
	    for (Object[] result : results) {
	        System.out.println("Timestamp: " + result[0] + ", Count: " + result[1]);
	    }
	    
	    Map<LocalDate, Long> rentCounts = new TreeMap<>();

	    // 결과를 맵에 저장
	    for (Object[] result : results) {
	    	// Object에서 Timestamp를 가져온 후 LocalDateTime으로 변환
	        Timestamp rentTimestamp = (Timestamp) result[0]; // Timestamp로 변환
	        LocalDateTime rentDate = rentTimestamp.toLocalDateTime(); // LocalDateTime으로 변환

	        // result[1]를 BigDecimal로 가져온 후 Long으로 변환
	        BigDecimal countBigDecimal = (BigDecimal) result[1];
	        Long count = countBigDecimal.longValue(); // BigDecimal에서 Long으로 변환

	        rentCounts.put(rentDate.toLocalDate(), count); // LocalDate로 변환
	        //System.out.println("Date: " + rentDate.toLocalDate() + ", Count: " + count);
	    }

	    // 누적 대여 건수 계산
	    long cumulativeCount = 0;
	    List<Object[]> cumulativeRents = new ArrayList<>();

	    // 시작 날짜부터 현재 날짜까지의 누적 대여 수 계산
	    LocalDateTime currentDateTime = startOfYear; // LocalDateTime으로 시작

	    while (!currentDateTime.isAfter(now)) { // 현재 날짜까지 반복
	        LocalDate date = currentDateTime.toLocalDate(); // LocalDate로 변환
	        Long dailyCount = rentCounts.getOrDefault(date, 0L);
	       
	        cumulativeCount += dailyCount; //cumulativeCount 잘 나옴.
	        cumulativeRents.add(new Object[]{currentDateTime, cumulativeCount}); // 현재 LocalDateTime 추가
	        currentDateTime = currentDateTime.plusDays(1); // 하루씩 증가
	        //System.out.println("Date: " + date + ", Daily Count: " + dailyCount + ", Cumulative Count: " + cumulativeCount); //확인용

	    }

	    return cumulativeRents;
	}
}

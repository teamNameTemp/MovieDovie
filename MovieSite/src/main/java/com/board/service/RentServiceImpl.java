package com.board.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	
	//관리자페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
	@Override
    public List<Object[]> RentCount(LocalDateTime startDate, LocalDateTime endDate){
    	 return rentRepository.RentCount(startDate, endDate);
    }
	

	//관리자페이지 - 통계 : 월별 대여 수 확인
    @Override
	public List<Object[]> RentCountByMonth(int year){
		return rentRepository.RentCountByMonth(year);
	}
	
	//관리자 페이지 - 통계 : 올해 대여 status 수 확인
	@Override
	public List<Object[]> RentStatus(){
		return rentRepository.RentStatus();
	}
	
	//관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	public List<Object[]> findTopRenters(){
        return rentRepository.findTopRenters();
	}
	
	
}

package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.entity.repository.RequestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

	private final RequestRepository requestRepository;
	
	//관리자페이지 : 통계 - 처리 상태 확인하기
	@Override	
	public List<Object[]> findStatus(){
		return requestRepository.findStatus();
	}
}

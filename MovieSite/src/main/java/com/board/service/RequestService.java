package com.board.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestService {

	//관리자페이지 : 통계 - 처리 상태 확인하기
	public List<Object[]> findStatus();
	
}

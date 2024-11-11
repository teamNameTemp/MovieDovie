package com.board.service;

import java.util.List;

public interface MemberLogService {

	//관리자페이지 - 통계 : 방문 수 확인
	public List<Object[]> visitResult();
}


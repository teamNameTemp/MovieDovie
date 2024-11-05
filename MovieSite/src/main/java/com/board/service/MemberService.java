package com.board.service;

import java.util.List;

import com.board.dto.MemberDTO;

public interface MemberService {
	
	//회원가입
	public void signup(MemberDTO member);
	
	//회원 정보 가져오기
	public MemberDTO memberInfo(String email);
	
	//관리자페이지 - 통계 : 회원 가입한 날 확인
	public List<Object[]> signupResult();
	
}

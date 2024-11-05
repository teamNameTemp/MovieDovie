package com.board.service;

import com.board.dto.MemberDTO;

public interface MemberService {
	
	//회원가입
	public void signup(MemberDTO member);
	
	//회원 정보 가져오기
	public MemberDTO memberInfo(String email);
	

}

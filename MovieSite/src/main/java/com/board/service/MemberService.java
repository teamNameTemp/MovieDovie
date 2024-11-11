package com.board.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.board.dto.MemberDTO;

public interface MemberService {

	//회원 가입
	public void signup(MemberDTO member);
	
	//회원 정보 가져 오기
	public MemberDTO memberInfo(String email);
	
	//이메일 중복 확인
	public int emailCheck(String email);
	
	//패스워드 수정
	public void modifyMemberPassword(MemberDTO member);
	
	//회원 로그인/로그아웃 로그 등록
	public void memberLogRegistry(String email, String statu);
	
	//마지막 로그인/로그아웃/패스워드 변경 날짜 등록 하기
	public void lastdateUpdate(String email, String status);
	
	//이메일 찾기
	public String searchEmail(MemberDTO member);
	

	
}

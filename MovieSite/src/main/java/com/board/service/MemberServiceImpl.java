package com.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.MemberEntity;
import com.board.entity.MemberLogEntity;
import com.board.entity.repository.MemberLogRepository;
import com.board.entity.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	private final MemberLogRepository memberLogRepository;
		
	//회원 가입
	@Override
	public void signup(MemberDTO member) {
		member.setRegdate(LocalDateTime.now());
		member.setLastpwdate(LocalDateTime.now());
		member.setPwcheck(1L);
		member.setRole("USER");	
		memberRepository.save(member.dtoToEntity(member));	
	}
	
	//회원 정보 가져 오기
	@Override
	public MemberDTO memberInfo(String email) {
		return memberRepository.findById(email).map((member) -> new MemberDTO(member)).get();
	}
	
	//이메일 중복 확인
	public int emailCheck(String email) {
		return memberRepository.findById(email).isEmpty()?0:1;
	};
	
	
	//회원 로그인/로그아웃 로그 등록
	@Override
	public void memberLogRegistry(String email, String status) {
		
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		MemberLogEntity memberLogEntity = MemberLogEntity.builder()
														 .email(memberEntity.getEmail())
														 .inouttime(LocalDateTime.now())
														 .status(status)
														 .build();		
		memberLogRepository.save(memberLogEntity);
	}
	//관리자페이지 - 통계 : 회원 가입한 날 확인
	@Override
	public List<Object[]> signupResult(){
		return memberRepository.signupResult();
	}

	
	//마지막 로그인/로그아웃/패스워드 변경일 등록 하기
	@Override
	public void lastdateUpdate(String email,String status) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		MemberLogEntity memberLogEntity = new MemberLogEntity();
		
		switch (status) {
		case "login":
		case "logout":
			memberLogEntity = MemberLogEntity.builder()
								.email(memberEntity.getEmail())
								.inouttime(LocalDateTime.now())
								.status(status).build();
			break;
		case "password":
			memberEntity.setLastpwdate(LocalDateTime.now());
			break;
		}
		memberRepository.save(memberEntity);
		memberLogRepository.save(memberLogEntity);
	}
	
	//이메일 찾기
	public String searchEmail(MemberDTO member) {
		return memberRepository.findByUsernameAndTelno(member.getUsername(),member.getTelno())
					.map(m-> m.getEmail()).orElse("ID_NOT_FOUND");
	}
	
	//비밀번호 찾기
	public String searchPassword(MemberDTO member) {
		return memberRepository.findByEmailAndUsername(member.getEmail(),member.getUsername())
					.map(m-> m.getEmail()).orElse("ID_NOT_FOUND");
	}

}

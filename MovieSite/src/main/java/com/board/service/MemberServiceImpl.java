package com.board.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.MemberEntity;
import com.board.entity.MemberLogEntity;
import com.board.entity.repository.FileRepository;
import com.board.entity.repository.MemberLogRepository;
import com.board.entity.repository.MemberRepository;
import com.board.util.PasswordMaker;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	private final MemberLogRepository memberLogRepository;
	private final FileRepository fileRepository;

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
	
	//회원 기본 정보 수정
	public void modifyMemberInfo(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.updateMemberInfo(member);
		memberRepository.save(memberEntity);
	}

    // 이메일 중복 확인
    @Override
    public int emailCheck(String email) {
    	// 이메일이 존재하면 1 반환, 없으면 0 반환
        return memberRepository.existsByEmail(email) ? 1 : 0;
    }

    // 닉네임 중복 확인
    @Override
    public int nicknameCheck(String nickname) {
    	// 닉네임이 존재하면 1 반환, 없으면 0 반환
        return memberRepository.existsByNickname(nickname) ? 1 : 0;
    }
	
	//패스워드 수정
	public void modifyMemberPassword(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setPassword(member.getPassword());
		memberRepository.save(memberEntity);	
	}
	
	
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
	


	


 
      
}

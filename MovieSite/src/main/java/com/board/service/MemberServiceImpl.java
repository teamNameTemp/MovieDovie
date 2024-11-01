package com.board.service;

import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.repository.MemberRepository;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;

	
	//회원가입
	@Override
	public void signup(MemberDTO member) {
		member.setRegdate(LocalDateTime.now());
		member.setLastpwdate(LocalDateTime.now());
		member.setPwcheck(1L);
		member.setRole("USER");
		memberRepository.save(member.dtoToEntity(member));
		
	}
	
	//회원 정보 가져오기
	@Override
	public MemberDTO memberInfo(String email) {
		return memberRepository.findById(email).map((member) -> new MemberDTO(member)).get();
	}
	
	
	
	

}

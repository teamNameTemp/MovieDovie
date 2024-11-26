package com.board.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.MemberDTO;
import com.board.entity.MemberEntity;
import com.board.entity.MemberLogEntity;
import com.board.entity.repository.MemberLogRepository;
import com.board.entity.repository.MemberRepository;
import com.board.util.DataCalculate;

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
		member.setGrade(DataCalculate.calcGrade(0));	
		
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
	
	
	//임시 패스워드 저장하기
	@Override	
	public void makeTempPW(String email, String newPW) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setPassword(newPW);
		memberRepository.save(memberEntity);	
	}
	
	// 회원 로그인/로그아웃 로그 등록
	@Override
	public void memberLogRegistry(String email, String status) {
	    MemberEntity memberEntity = memberRepository.findById(email).orElseThrow(() -> 
	        new RuntimeException("회원 정보가 존재하지 않습니다.")
	    );

	    MemberLogEntity memberLogEntity = MemberLogEntity.builder()
	                                                     .email(memberEntity.getEmail())
	                                                     .inouttime(LocalDateTime.now())
	                                                     .status(status)
	                                                     .build();

	    memberLogRepository.save(memberLogEntity);
	}

	// 마지막 로그인/로그아웃/패스워드 변경일 등록
	@Override
	public void lastdateUpdate(String email, String status) {
	    MemberEntity memberEntity = memberRepository.findById(email).orElseThrow(() -> 
	        new RuntimeException("회원 정보가 존재하지 않습니다.")
	    );

	    MemberLogEntity memberLogEntity = null;

	    if ("login".equals(status) || "logout".equals(status)) {
	        memberLogEntity = MemberLogEntity.builder()
	                                         .email(memberEntity.getEmail())
	                                         .inouttime(LocalDateTime.now())
	                                         .status(status)
	                                         .build();
	    } else if ("password".equals(status)) {
	        memberEntity.setLastpwdate(LocalDateTime.now());
	    }

	    memberRepository.save(memberEntity);

	    if (memberLogEntity != null) {
	        memberLogRepository.save(memberLogEntity);
	    }
	}

	
	//이메일 찾기
	public String searchEmail(MemberDTO member) {
		return memberRepository.findByUsernameAndTelno(member.getUsername(),member.getTelno())
					.map(m-> m.getEmail()).orElse("ID_NOT_FOUND");
	}
	


	//관리자페이지 - 통계 : 회원 가입한 날 확인
	@Override
	public List<Object[]> signupResult(){
		return memberRepository.signupResult();
	}

	//관리자페이지 - 통계 : 최근 7일간 회원가입한 회원 수 확인
	@Override	
	public int newMembersInLastSevenDays(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo) {
		return memberRepository.newMembersInLastSevenDays(sevenDaysAgo);
	}

	@Override
	public void modifyMemberProfile(MemberDTO member){
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();

		memberEntity.setFilesize(member.getFilesize());
		memberEntity.setOrg_filename(member.getOrgFilename());
		memberEntity.setStored_filename(member.getStoredFilename());
		
		memberRepository.save(memberEntity);
	}

}

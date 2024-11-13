package com.board.dto;

import java.time.LocalDateTime;

import com.board.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberDTO {
	private String email;
	private String password;
	private String username;
	private String nickname;
	private String gender;
	private String birth;
	private String telno;
	private LocalDateTime regdate;
	private LocalDateTime lastpwdate;
	private Long pwcheck;
	private String role;
	
	//Entity -> DTO
	public MemberEntity dtoToEntity(MemberDTO memberDTO) {
		MemberEntity memberEntity = MemberEntity.builder()
											.email(memberDTO.getEmail())
											.password(memberDTO.getPassword())
											.username(memberDTO.getUsername())
											.nickname(memberDTO.getNickname())
											.gender(memberDTO.getGender())
											.birth(memberDTO.getBirth())
											.telno(memberDTO.getTelno())
											.regdate(memberDTO.getRegdate())
											.lastpwdate(memberDTO.getLastpwdate())
											.pwcheck(memberDTO.getPwcheck())
											.role(memberDTO.getRole())
											.build();
		return memberEntity;
	}
	 
	//DTO -> Entity
	public MemberDTO(MemberEntity memberEntity) {
		this.email = memberEntity.getEmail();
		this.password = memberEntity.getPassword();
		this.username = memberEntity.getUsername();
		this.nickname = memberEntity.getNickname();
		this.gender = memberEntity.getGender();
		this.birth = memberEntity.getBirth();
		this.telno = memberEntity.getTelno();
		this.regdate= memberEntity.getRegdate();
		this.lastpwdate = memberEntity.getLastpwdate();
		this.pwcheck = memberEntity.getPwcheck();
		this.role = memberEntity.getRole();
		
		
	}
}

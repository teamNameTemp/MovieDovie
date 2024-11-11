package com.board.dto;

import java.time.LocalDateTime;

import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;

import com.board.entity.MemberEntity;
import com.board.entity.RentEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
	private Long logseqno;
	private Long genreseqno;
	
	
	//DTO -> Entity
	public MemberDTO(MemberEntity memberEntity) {
		this.email = memberEntity.getEmail();
		this.password = memberEntity.getPassword();
		this.username = memberEntity.getUsername();
		this.nickname = memberEntity.getNickname();
		this.gender = memberEntity.getGender();
		this.birth = memberEntity.getBirth();
		this.telno = memberEntity.getTelno();
		this.regdate = memberEntity.getRegdate();
		this.lastpwdate = memberEntity.getLastpwdate();
		this.pwcheck = memberEntity.getPwcheck();
		this.role = memberEntity.getRole();
		this.logseqno = memberEntity.getLogseqno();
		this.genreseqno = memberEntity.getGenreseqno();
	}
		
	//DTO --> Entity 이동
	public MemberEntity dtoToEntity(MemberDTO dto) {
		
		MemberEntity memberEntity = MemberEntity.builder()
											.email(dto.getEmail())
											.password(dto.getPassword())
											.username(dto.getUsername())
											.nickname(dto.getNickname())
											.gender(dto.getGender())
											.birth(dto.getBirth())
											.telno(dto.getTelno())
											.regdate(dto.getRegdate())
											.lastpwdate(dto.getLastpwdate())
											.pwcheck(dto.getPwcheck())
											.role(dto.getRole())
											.logseqno(dto.getLogseqno())
											.genreseqno(dto.getGenreseqno())
											.build();									
		return memberEntity;
	}
}

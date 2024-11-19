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
	private String ssn;
	private String telno;
	private String description;
	private LocalDateTime regdate;
	private LocalDateTime lastlogindate;
	private LocalDateTime lastlogoutdate;
	private LocalDateTime lastpwdate;
	private Long pwcheck;
	private String role;
	private String orgFilename;
	private String storedFilename;
	private long filesize;
	private Long point;
	private Long logseqno;
	private Long genreseqno;
	
	
	//Entity -> DTO
	public MemberDTO(MemberEntity memberEntity) {
		this.email = memberEntity.getEmail();
		this.password = memberEntity.getPassword();
		this.username = memberEntity.getUsername();
		this.nickname = memberEntity.getNickname();
		this.gender = memberEntity.getGender();
		this.ssn = memberEntity.getSsn();
		this.telno = memberEntity.getTelno();
		this.description = memberEntity.getDescription();
		this.regdate= memberEntity.getRegdate();
		this.lastlogindate = memberEntity.getLastlogindate();
		this.lastlogoutdate = memberEntity.getLastlogoutdate();
		this.lastpwdate = memberEntity.getLastpwdate();
		this.pwcheck = memberEntity.getPwcheck();
		this.role = memberEntity.getRole();
		this.orgFilename = memberEntity.getOrg_filename();
		this.storedFilename = memberEntity.getStored_filename();
		this.filesize = memberEntity.getFilesize();
		this.point=memberEntity.getPoint();
		this.regdate = memberEntity.getRegdate();
		this.lastpwdate = memberEntity.getLastpwdate();
		this.pwcheck = memberEntity.getPwcheck();
		this.role = memberEntity.getRole();
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
											.build();									
		return memberEntity;
	}
}

package com.board.entity;

import java.time.LocalDateTime;

import com.board.dto.MemberDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name="member")
@Table(name="tbl_member")
public class MemberEntity {
	
	//Column(name="컬럼명", length=길이, nullable=null여부(false : not null / true : null)
	@Id
	@Column(name="email", length=320, nullable=false)
	private String email;
	
	@Column(name="password", length=200, nullable=false)
	private String password;
	
	@Column(name="username", length=50, nullable=false)
	private String username;
	
	@Column(name="nickname", length=50, nullable=false)
	private String nickname;
	
	@Column(name="gender", length=20, nullable=false)
	private String gender;
	
	@Column(name="ssn", nullable=true)
	private String ssn;
	
	@Column(name="telno", length=50, nullable=false)
	private String telno;
	
	@Column(name="description",length=2000,nullable=true)
	private String description;
	
	@Column(name="regdate", nullable=false)
	private LocalDateTime regdate;
	
	@Column(name="lastlogindate", nullable=true)
	private LocalDateTime lastlogindate;
	
	@Column(name="lastlogoutdate", nullable=true)
	private LocalDateTime lastlogoutdate;
	
	@Column(name="lastpwdate", nullable=true)
	private LocalDateTime lastpwdate;
	
	@Column(name="pwcheck", nullable=true)
	private Long pwcheck;
	
	@Column(name="role", length=50, nullable=false)
	private String role;
	
	@Column(name="org_filename")
	private String org_filename;
	
	@Column(name="stored_filename")
	private String stored_filename;
	
	@Column(name="filesize",nullable=true)
	private Long filesize;
	
	@Column(name="memeberPoint")
	private Long point;
	
	public void updateMemberInfo(MemberDTO member) {
		
		this.email = member.getEmail();
		this.username = member.getUsername();
		this.nickname = member.getNickname();
		this.gender = member.getGender();
		this.ssn = member.getSsn();
		this.telno = member.getTelno();
		this.filesize = member.getFilesize();
		this.point=member.getPoint();
		
	}

	
	

}

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
	
	@Column(name="birth", nullable=false)
	private String birth;
	
	@Column(name="telno", length=50, nullable=false)
	private String telno;
	
	@Column(name="regdate", nullable=false)
	private LocalDateTime regdate;
	
	@Column(name="lastpwdate", nullable=true)
	private LocalDateTime lastpwdate;
	
	@Column(name="pwcheck", nullable=true)
	private Long pwcheck;
	
	@Column(name="role", length=50, nullable=false)
	private String role;
	
	public void updateMemberInfo(MemberDTO member) {
		
		this.username = member.getUsername();
		this.nickname = member.getNickname();
		this.gender = member.getGender();
		this.birth = member.getBirth();
		this.telno = member.getTelno();
		
	}

	
	

}

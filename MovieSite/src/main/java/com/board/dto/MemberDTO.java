package com.board.dto;

import java.time.LocalDateTime;

import com.board.entity.MemberEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
	private String email;
	private String password;
	private String username;
	private String nickname;
	private String gender;
	private LocalDateTime birth;
	private String telno;
	private LocalDateTime regdate;
	private LocalDateTime lastpwdate;
	private Long pwcheck;
	private String role;
	
	//Entity -> DTO
	public MemberDTO(MemberEntity entity) {
		this.email = entity.getEmail();
	}
	
	//DTO -> Entity
//	public MemberEntity dtoToEntity(MemberDTO dto)
//	{
//	}
}

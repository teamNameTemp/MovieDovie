package com.board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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

	//DTO -> Entity
	
}

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
	
	@Column(name="birth", nullable=true)
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
	
	
	/*
	//FK 만들기
	//FK 읽어올 때 Eager, Lazy 두 가지 타입이 있음.
	//Eager : 부모키가 있는 테이블부터 검사해서 부모키가 제대로 되어 있는지 확인하고 자식키를 읽음 -> 정확도는 높지만 성능이 저하.
	//Lazy : 자식키가 있는 테이블만 읽음 -> 정확도는 떨어지지만 성능이 향상.
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "logseqno", referencedColumnName = "seqno", nullable = false)
	private MemberLogEntity logseqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "genreseqno", referencedColumnName = "seqno", nullable = false)
	private GenreEntity genreseqno;
	*/
	
	public void updateMemberInfo(MemberDTO member) {
		
		this.username = member.getUsername();
		this.nickname = member.getNickname();
		this.gender = member.getGender();
		this.birth = member.getBirth();
		this.telno = member.getTelno();
		
	}

	
	

}

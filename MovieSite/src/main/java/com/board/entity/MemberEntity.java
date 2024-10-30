package com.board.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="member")
@Table(name="tbl_member")
public class MemberEntity {
	
	//Column(name="컬럼명", length=길이, nullable=null여부(false : not null / true : null)
	@Id
	@Column(name="email", length=320, nullable=false)
	private String email;
	

}

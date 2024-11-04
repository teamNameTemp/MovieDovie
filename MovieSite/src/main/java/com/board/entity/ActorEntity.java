package com.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "actor")
@Table(name="tbl_actor")
public class ActorEntity {
	@Id
	@Column(name="code",nullable=false)
	private Long seqno;
	
	@Column(name="name", length=200, nullable=false)
	private String name;
	
	@Column(name="nameen",length=200,nullable=false)
	private String nameen;
	
	@Column(name="role",length=50,nullable=true)
	private String role;
	
	@Column(name="gender",length=20,nullable=false)
	private String gender;
}

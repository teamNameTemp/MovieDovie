package com.board.entity.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long seqno;
	
	@Column(name="peopleNm")
	private String peopleNm;
	
	@Column(name="peopleNmEn")
	private String peopleNmEn;
	
	@Column(name="cast")
	private String cast;
	
	@Column(name="castEn")
	private String castEn;
}

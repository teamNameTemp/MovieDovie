package com.board.entity.movie;

import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "actor")
@Table(name="tbl_actor")
public class ActorEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACTOR_SEQ")
	@SequenceGenerator(name="ACTOR_SEQ", sequenceName = "tbl_actor_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name="peopleNm",nullable=true)
	private String peopleNm;
	
	@Column(name="peopleNmEn",nullable=true)
	private String peopleNmEn;
	
	@Column(name="cast",nullable=true)
	private String cast;
	
	@Column(name="castEn",nullable=true)
	private String castEn;
	

	public ActorEntity(Map<String, Object> data) {
		this.peopleNm = (String)data.get("peopleNm");
		this.peopleNmEn = (String)data.get("peopleNmEn");
		this.cast = (String)data.get("cast");
		this.castEn = (String)data.get("castEn");
	}
}

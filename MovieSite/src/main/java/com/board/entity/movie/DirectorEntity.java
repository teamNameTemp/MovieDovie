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
@Entity(name="director")
@Table(name="tbl_director")
public class DirectorEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long seqno;
	
	@Column(name="peopleNm")
	private String peopleNm;
	
	@Column(name="peopleNmEn")
	private String peopleNmEn;
}

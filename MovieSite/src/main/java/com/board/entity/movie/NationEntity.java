package com.board.entity.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="nation")
@Table(name="tbl_nation")
public class NationEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long seqno;
	
	@Column(name="nationNm")
	private String nationNm;
}

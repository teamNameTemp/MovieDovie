package com.board.entity.movie;

import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity(name="nation")
@Table(name="tbl_nation")
public class NationEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="NATION_SEQ")
	@SequenceGenerator(name="NATION_SEQ", sequenceName = "tbl_nation_sql", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name="nationNm")
	private String nationNm;
	
	public NationEntity(Map<String,Object> data)
	{
		this.nationNm = (String)data.get("nationNm");
	}
}

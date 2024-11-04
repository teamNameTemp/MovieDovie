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
@Entity(name="director")
@Table(name="tbl_director")
public class DirectorEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIRECTOR_SEQ")
	@SequenceGenerator(name="DIRECTOR_SEQ", sequenceName = "tbl_director_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name="peopleNm")
	private String peopleNm;
	
	@Column(name="peopleNmEn")
	private String peopleNmEn;
	
	public DirectorEntity(Map<String,Object> data)
	{
		this.peopleNm = (String)data.get("peopleNm");
		this.peopleNmEn = (String)data.get("peopleNmEn");
	}
}

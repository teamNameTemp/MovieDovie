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

@Getter
@Entity(name="genre")
@Table(name="tbl_genre")
public class GenreEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="GENRE_SEQ")
	@SequenceGenerator(name="GENRE_SEQ",sequenceName="tbl_genre_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name="code")
	private String code;
	
	@Column(name="genre")
	private String genre;
	
	public GenreEntity(Map<String,Object> data)
	{
		this.code = (String)data.get("code");
		this.genre = (String)data.get("genre");
	}
}

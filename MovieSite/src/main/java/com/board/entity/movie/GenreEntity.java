package com.board.entity.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity(name="genre")
@Table(name="tbl_genre")
public class GenreEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long seqno;
	
	@Column(name="code")
	private String code;
	
	@Column(name="genre")
	private String genre;
	
}

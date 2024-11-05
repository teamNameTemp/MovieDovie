package com.board.entity.movie;

import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name="genre")
@Table(name="tbl_genre")
@NoArgsConstructor
public class GenreEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="GENRE_SEQ")
	@SequenceGenerator(name="GENRE_SEQ",sequenceName="tbl_genre_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name="code")
	private String code;
	
	@Column(name="genrenm")
	private String genreNm;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="movie_cd")
	private MovieEntity movie;
}

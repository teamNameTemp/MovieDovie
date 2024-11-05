package com.board.entity.movie;

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
@Entity(name="nation")
@Table(name="tbl_nation")
@NoArgsConstructor
public class NationEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="NATION_SEQ")
	@SequenceGenerator(name="NATION_SEQ", sequenceName = "tbl_nation_sql", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name="nationNm")
	private String nationNm;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="movie_cd")
	private MovieEntity movie;
	
}

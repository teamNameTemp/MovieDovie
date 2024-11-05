package com.board.entity.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude="movies")
@Getter
@Setter
@Entity(name = "actor")
@Table(name="tbl_actor")
@NoArgsConstructor
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="movie_cd",nullable=true)
	private MovieEntity movie;
}

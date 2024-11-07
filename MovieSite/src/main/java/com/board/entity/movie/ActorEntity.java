package com.board.entity.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity(name = "actors")
@Table(name = "tbl_actor")
@NoArgsConstructor
@AllArgsConstructor
public class ActorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTOR_SEQ")
	@SequenceGenerator(name = "ACTOR_SEQ", sequenceName = "tbl_actor_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	// 배우 ID
	@Column(name = "id", nullable = true)
	private int id;
	// 배우 이름
	@Column(name = "name", nullable = true)
	private String name;

	// 배역 이름
	@Column(name = "characterName", nullable = true)
	private String characterName;
	// 배우 프로필 이미지 경로
	@Column(name = "profilePath", nullable = true)
	private String profilePath;
	// 출연 순서
	@Column(name = "order", nullable = true)
	private int order;

	@ManyToMany(mappedBy = "actors")
	private List<MovieEntity> movies;
}

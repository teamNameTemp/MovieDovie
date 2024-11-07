package com.board.entity.movie;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "actors")
@Table(name = "jpa_actor")
@NoArgsConstructor
public class ActorEntity {
	// 배우 ID
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
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

//		@ManyToMany(mappedBy = "actors")
//		private List<MovieEntity> movies;
}
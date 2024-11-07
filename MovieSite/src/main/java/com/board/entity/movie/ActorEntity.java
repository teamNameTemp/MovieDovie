package com.board.entity.movie;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "actors")
@Table(name = "jpa_actor")
@NoArgsConstructor
@Builder
@AllArgsConstructor
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
	@Column(name = "orderNum", nullable = true)
	private int order;

	@ManyToMany(mappedBy = "actors")
	private List<MovieEntity> movies = new ArrayList<>();
	
	public void addMovies(MovieEntity entity) {
		movies.add(entity);
		if(!entity.getActors().contains(this))
		{
			entity.getActors().add(this);
		}
	}
}
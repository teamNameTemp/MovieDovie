package com.board.entity.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "crews")
@Table(name = "jpa_crew")
@NoArgsConstructor
public class CrewEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 제작진 이름
	@Column(name = "name", nullable = true)
	private String name;
	// 직책
	@Column(name = "jobTitle", nullable = true)
	private String jobTitle;
	// 부서
	@Column(name = "department", nullable = true)
	private String department;
	// 제작진 프로필 이미지 경로
	@Column(name = "profilePath", nullable = true)
	private String profilePath;

//	@ManyToOne
//	@JoinColumn(name = "movie_id", nullable = false)
//	private MovieEntity movie;

}

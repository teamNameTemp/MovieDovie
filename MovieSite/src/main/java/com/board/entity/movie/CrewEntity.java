package com.board.entity.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "crews")
@Table(name = "tbl_crew")
@NoArgsConstructor
@AllArgsConstructor
public class CrewEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREW_SEQ")
	@SequenceGenerator(name = "CREW_SEQ", sequenceName = "tbl_crew_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	// 제작진 ID
	@Column(name = "id", nullable = true)
	private int id;
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

	@ManyToMany(mappedBy = "crews")
	private List<MovieEntity> movies;
}

package com.board.entity.movie;

import java.util.List;

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
@Entity(name="companys")
@Table(name="jpa_company")
@NoArgsConstructor
public class CompanyEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Integer id;
		// 제작사 로고 경로
		@Column(name="logoPath", nullable = true)
		private String logoPath;
		// 제작사 이름
		@Column(name="name",nullable = true)
		private String name;
		// 제작사 국가
		@Column(name="originCountry",nullable = true)
		private String originCountry;
		
//		@ManyToMany(mappedBy = "companys")
//		private List<MovieEntity> movies;
}



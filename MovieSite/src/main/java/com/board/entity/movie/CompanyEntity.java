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
@Table(name="tbl_company")
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQ")
		@SequenceGenerator(name="COMPANY_SEQ", sequenceName = "tbl_company_seq", initialValue = 1, allocationSize = 1)
		private Long seqno;
		
		// 제작사 ID
		@Column(name="id", nullable = true)
		private int id;
		// 제작사 로고 경로
		@Column(name="logoPath", nullable = true)
		private String logoPath;
		// 제작사 이름
		@Column(name="name",nullable = true)
		private String name;
		// 제작사 국가
		@Column(name="originCountry",nullable = true)
		private String originCountry;
		
		@ManyToMany(mappedBy = "comapanys")
		private List<MovieEntity> movies;
}



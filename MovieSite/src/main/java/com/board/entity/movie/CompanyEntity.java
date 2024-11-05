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
@Entity(name="company")
@Table(name="tbl_company")
@NoArgsConstructor
public class CompanyEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQ")
		@SequenceGenerator(name="COMPANY_SEQ", sequenceName = "tbl_company_seq", initialValue = 1, allocationSize = 1)
		private long seqno;
		
		@Column(name="companyCd")
		private String companyCd;
		
		@Column(name="companyNm")
		private String companyNm;
		
		@Column(name="companyNmEn")
		private String companyNmEn;
		
		@Column(name="companyPartNm")
		private String companyPartNm;
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="movie_cd")
		private MovieEntity movie;
}



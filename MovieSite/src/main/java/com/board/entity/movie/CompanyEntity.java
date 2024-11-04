package com.board.entity.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="company")
@Table(name="tbl_company")
public class CompanyEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQ")
		@SequenceGenerator(name="COMPANY_SEQ", sequenceName = "tbl_company_seq", initialValue = 1, allocationSize = 1)
		private long seqno;
		
		@Column(name="companyCd")
		private String companuCd;
		
		@Column(name="companyNm")
		private String companyNm;
		
		@Column(name="companyNmEn")
		private String companyNmEn;
		
		@Column(name="companyPartNm")
		private String companyPartNm;
}

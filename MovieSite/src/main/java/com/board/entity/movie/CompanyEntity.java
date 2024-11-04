package com.board.entity.movie;

import java.util.Map;

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
		private String companyCd;
		
		@Column(name="companyNm")
		private String companyNm;
		
		@Column(name="companyNmEn")
		private String companyNmEn;
		
		@Column(name="companyPartNm")
		private String companyPartNm;

		public CompanyEntity(Map<String,Object> data)
		{
			this.companyCd = (String)data.get("companyCd");
			this.companyNm = (String)data.get("companyNm");
			this.companyNmEn = (String)data.get("companyNmEn");
			this.companyPartNm = (String)data.get("companyPartNm");
		}
}



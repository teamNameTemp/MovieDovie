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
@Entity(name = "spokenLanguage")
@Table(name = "tbl_spokenLanguage")
@NoArgsConstructor
@AllArgsConstructor
public class SpokenlanguageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUNTRY_SEQ")
	@SequenceGenerator(name = "COUNTRY_SEQ", sequenceName = "tbl_country_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;

	@Column(name = "englishName", nullable = true)
	private String englishName;
	// 언어 코드 (ISO 639-1)
	@Column(name = "iso_639_1", nullable = true)
	private String iso639_1;
	// 언어 이름
	@Column(name = "name", nullable = true)
	private String name;
	
	
	@ManyToMany(mappedBy = "spokenLanguage")
	private List<MovieEntity> movies;
}

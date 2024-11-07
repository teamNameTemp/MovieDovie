package com.board.entity.movie;

import java.util.ArrayList;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "spokenLanguages")
@Table(name = "jpa_spokenLanguage")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SpokenLanguageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPOKEN_SEQ")
	@SequenceGenerator(name = "SPOKEN_SEQ", sequenceName = "jpa_spoken_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;

	@Column(name = "englishName", nullable = true)
	private String englishName;
	// 언어 코드 (ISO 639-1)
	@Column(name = "iso_639_1", nullable = true)
	private String iso639_1;
	// 언어 이름
	@Column(name = "name", nullable = true)
	private String name;
	
	
	@ManyToMany(mappedBy = "spokenLanguages")
	private List<MovieEntity> movies = new ArrayList<>();
	
	public void addMovies(MovieEntity entity) {
		movies.add(entity);
	}
}

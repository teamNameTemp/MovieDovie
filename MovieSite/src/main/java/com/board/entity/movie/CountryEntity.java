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
@Entity(name = "countrys")
@Table(name = "jpa_country")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CountryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUNTRY_SEQ")
	@SequenceGenerator(name = "COUNTRY_SEQ", sequenceName = "jpa_country_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;

	// 국가 코드 (ISO 3166-1)
	@Column(name="iso3166_1",nullable = true)
	private String iso3166_1;
	// 국가 이름
	@Column(name="name",nullable = true)
	private String name;
	
	@ManyToMany(mappedBy = "countrys")
	private List<MovieEntity> movies = new ArrayList<>();
	
	public void addMovies(MovieEntity entity) {
		 movies.add(entity);
		 if(!entity.getCountrys().contains(this))
		 {
			 entity.getCountrys().add(this);
		 }
	}
}

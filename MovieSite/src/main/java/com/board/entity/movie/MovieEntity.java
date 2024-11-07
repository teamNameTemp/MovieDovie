package com.board.entity.movie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "movie")
@Table(name = "jpa_movie")
@NoArgsConstructor
public class MovieEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")
	@SequenceGenerator(name = "MOVIE_SEQ", sequenceName = "jpa_movie_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;

	@Column(name = "id", nullable = true)
	private Integer id;
	@Column(name = "title", nullable = true)
	private String title;

	@Column(name = "original_title", nullable = true)
	private String originalTitle;

	@Column(name = "tagline", nullable = true)
	private String tagline;

	@Column(name = "overview", nullable = true)
	private String overview;

	@Column(name = "runtime", nullable = true)
	private Integer runtime;

	@Column(name = "backdrop_path", nullable = true)
	private String backdropPath;

	@Column(name = "poster_path", nullable = true)
	private String posterPath;

	@Column(name="video", nullable = true)
	private boolean video;
	@Column(name = "original_language", nullable = true)
	private String originalLanguage;
	@Column(name = "imdb_id", nullable = true)
	private String imdbId;
	@Column(name = "popularity", nullable = true)
	private double popularity;
	@Column(name = "release_date", nullable = true)
	private LocalDate releaseDate; // Released 개봉된
	@Column(name = "status", nullable = true)
	private String status;
	@Column(name = "vote_average", nullable = true)
	private String voteAverage;
	@Column(name = "vote_count", nullable = true)
	private int voteCount;
	//==========================================================================
	
	//==========================================================================
//	@ManyToMany
//	@JoinTable(name = "movie_genres", 
//	joinColumns = @JoinColumn(name = "movie_id"), 
//	inverseJoinColumns = @JoinColumn(name = "jpa_genre_id"))
//	private List<GenreEntity> genres =new ArrayList<>();
//
//	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<VideoEntity> videos = new ArrayList<>();
//
//	@ManyToMany
//	@JoinTable(name = "movie_companys", 
//	joinColumns = @JoinColumn(name = "movie_id"), 
//	inverseJoinColumns = @JoinColumn(name = "jpa_company_id"))
//	private List<CompanyEntity> companys = new ArrayList<>();
//
//	@ManyToMany
//	@JoinTable(name = "movie_countrys", 
//	joinColumns = @JoinColumn(name = "movie_id"), 
//	inverseJoinColumns = @JoinColumn(name = "jpa_country_id"))
//	private List<CountryEntity> countrys = new ArrayList<>();
//
//	@ManyToMany
//	@JoinTable(name = "movie_spokenLanguages", 
//	joinColumns = @JoinColumn(name = "movie_id"), 
//	inverseJoinColumns = @JoinColumn(name = "jpa_spokenLanguage_id"))
//	private List<SpokenLanguageEntity> spokenLanguages = new ArrayList<>();
//
//	@ManyToMany
//	@JoinTable(name = "movie_actors", 
//	joinColumns = @JoinColumn(name = "movie_id"), 
//	inverseJoinColumns = @JoinColumn(name = "jpa_actor_id"))
//	private List<ActorEntity> actors = new ArrayList<>();
//
//	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<CrewEntity> crews = new ArrayList<>();
//	
	
}
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "movie")
@Table(name = "jpa_movie")
public class MovieEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")
	@SequenceGenerator(name = "MOVIE_SEQ", sequenceName = "jpa_movie_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;

	@Column(name="id", nullable=true)
	private int id;
	@Column(name = "title", nullable = true)
	private String title;

	@Column(name = "original_title", nullable = true)
	private String originalTitle;

	@Column(name = "tagline", nullable = true)
	private String tagline;

	@Column(name = "overview", nullable = true)
	private String overview;

	@Column(name = "runtime", nullable = true)
	private String runtime;

	@Column(name = "backdrop_path", nullable = true)
	private String backDropPath;

	@Column(name = "poster_path", nullable = true)
	private String posterPath;

	@Column(name = "original_language", nullable = true)
	private String originalLanguage;
	@Column(name = "imdb_id", nullable = true)
	private String imdbId;
	@Column(name = "popularity", nullable = true)
	private String popularity;
	@Column(name = "release_date", nullable = true)
	private LocalDate releaseDate; //Released 개봉된
	@Column(name = "status", nullable = true)
	private String status;
	@Column(name = "vote_average", nullable = true)
	private String voteAverage;
	@Column(name = "vote_count", nullable = true)
	private int voteCount;

	@ManyToMany
	@JoinTable(
		name="movie_genres",
		joinColumns = @JoinColumn(name="movie_id"),
		inverseJoinColumns = @JoinColumn(name="genre_id")
	)
	private List<GenreEntity> genres;

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VideoEntity> videos = new ArrayList<>();
	
	 @ManyToMany
	    @JoinTable(
	        name="movie_companys",
	        joinColumns = @JoinColumn(name="movie_id"),
	        inverseJoinColumns = @JoinColumn(name="company_id")
	    )
	    private List<CompanyEntity> companys = new ArrayList<>();

	    @ManyToMany
	    @JoinTable(
	        name="movie_production_countries",
	        joinColumns = @JoinColumn(name="movie_id"),
	        inverseJoinColumns = @JoinColumn(name="country_id")
	    )
	    private List<CountryEntity> productionCountries = new ArrayList<>();

	    @ManyToMany
	    @JoinTable(
	        name="movie_spoken_languages",
	        joinColumns = @JoinColumn(name="movie_id"),
	        inverseJoinColumns = @JoinColumn(name="language_id")
	    )
	    private List<SpokenlanguageEntity> spokenLanguages = new ArrayList<>();

	    @ManyToMany
	    @JoinTable(
	        name="movie_actors",
	        joinColumns = @JoinColumn(name="movie_id"),
	        inverseJoinColumns = @JoinColumn(name="actor_id")
	    )
	    private List<ActorEntity> actor = new ArrayList<>();

	    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<CrewEntity> crew = new ArrayList<>();
}
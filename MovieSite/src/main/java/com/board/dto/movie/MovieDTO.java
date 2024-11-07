package com.board.dto.movie;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieDTO {
	private boolean adult;
	@JsonProperty("backdrop_path")
	private String backdropPath;
	@JsonProperty("belongs_to_collection")
	private CollectionInfo belongsToCollection;
	private long budget;
	private List<Genre> genres;
	private String homepage;
	private int id;
	@JsonProperty("imdb_id")
	private String imdbId;
	@JsonProperty("original_language")
	private String originalLanguage;
	@JsonProperty("original_title")
	private String originalTitle;
	private String overview;
	private double popularity;
	@JsonProperty("poster_path")
	private String posterPath;
	@JsonProperty("production_companies")
	private List<ProductionCompany> productionCompanies;
	@JsonProperty("production_countries")
	private List<ProductionCountry> productionCountries;
	@JsonProperty("release_date")
	private LocalDate releaseDate;
	private long revenue;
	private Integer runtime;
	@JsonProperty("spoken_languages")
	private List<SpokenLanguage> spokenLanguages;
	private String status;
	private String tagline;
	private String title;
	private boolean video;
	@JsonProperty("vote_average")
	private double voteAverage;
	@JsonProperty("vote_count")
	private int voteCount;

	// Additional fields that might be included in some TMDB responses
	@JsonProperty("keywords")
	private Keywords keywords;

	@Data
	public static class Keywords {
	    private List<Keyword> keywords;
	}

	@Data
	public static class Keyword {
	    private int id;
	    private String name;
	}
	
	@JsonProperty("videos")
	private Videos videos;

	@JsonProperty("images")
	private Images images;

	@Data
	public static class Videos {
	    private List<Video> results;
	}

	@Data
	public static class Images {
	    private List<Image> backdrops;
	    private List<Image> posters;
	}
	@JsonProperty("credits")
	private Credits credits;

	@Data
	public static class Credits {
	    private List<Cast> cast;
	    private List<Crew> crew;
	}
	private List<Review> reviews;
	@JsonProperty("similar_movies")
	private List<MovieBrief> similarMovies;
	@JsonProperty("recommended_movies")
	private List<MovieBrief> recommendedMovies;

	@Data
	public static class CollectionInfo {
		private int id;
		private String name;
		@JsonProperty("poster_path")
		private String posterPath;
		@JsonProperty("backdrop_path")
		private String backdropPath;
	}

	@Data
	public static class Genre {
		private int id;
		private String name;
	}

	@Data
	public static class ProductionCompany {
		private int id;
		@JsonProperty("logo_path")
		private String logoPath;
		private String name;
		@JsonProperty("origin_country")
		private String originCountry;
	}

	@Data
	public static class ProductionCountry {
		@JsonProperty("iso_3166_1")
		private String iso3166_1;
		private String name;
	}

	@Data
	public static class SpokenLanguage {
		@JsonProperty("english_name")
		private String englishName;
		@JsonProperty("iso_639_1")
		private String iso639_1;
		private String name;
	}

	@Data
	public static class Video {
		private String id;
		@JsonProperty("iso_639_1")
		private String iso639_1;
		@JsonProperty("iso_3166_1")
		private String iso3166_1;
		private String key;
		private String name;
		private String site;
		private int size;
		private String type;
	}

	@Data
	public static class Image {
		@JsonProperty("aspect_ratio")
		private double aspectRatio;
		@JsonProperty("file_path")
		private String filePath;
		private int height;
		@JsonProperty("iso_639_1")
		private String iso639_1;
		@JsonProperty("vote_average")
		private double voteAverage;
		@JsonProperty("vote_count")
		private int voteCount;
		private int width;
	}

	@Data
	public static class Cast {
		private int id;
		private String name;
		@JsonProperty("character")
		private String characterName;
		@JsonProperty("profile_path")
		private String profilePath;
		private int order;
	}

	@Data
	public static class Crew {
		private int id;
		private String name;
		@JsonProperty("job")
		private String jobTitle;
		private String department;
		@JsonProperty("profile_path")
		private String profilePath;
	}

	@Data
	public static class Review {
		private String id;
		private String author;
		private String content;
		private String url;
	}

	@Data
	public static class MovieBrief {
		private int id;
		private String title;
		@JsonProperty("poster_path")
		private String posterPath;
		@JsonProperty("release_date")
		private LocalDate releaseDate;
	}
}
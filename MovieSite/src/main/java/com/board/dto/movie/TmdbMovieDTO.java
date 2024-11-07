package com.board.dto.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class TmdbMovieDTO {
	@Data
	public static class MovieSearchResponse {
	    private List<Movie> results;
	}

	@Data
	public static class Movie {
	    private Long id;
	    private String title;
	    private String overview;
	    @JsonProperty("poster_path")
	    private String posterPath;
	    @JsonProperty("release_date")
	    private String releaseDate;
	}
}

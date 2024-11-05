package com.board.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.board.dto.movie.ActorDetailDTO;
import com.board.dto.movie.TmdbMovieDTO.Movie;
import com.board.dto.movie.TmdbMovieDTO.MovieSearchResponse;
import com.board.entity.movie.GenreEntity;
import com.board.entity.movie.MovieEntity;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class TmdbAPIService {
	@Value("${tmdb.api.base-url}")
	private String baseUrl;
	
	@Value("${tmdb.api.token}")
	private String token;
	
    private WebClient webClient;
    public TmdbAPIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("https://api.themoviedb.org/3")
            .defaultHeader("accept", "application/json")
            .defaultHeader("Accept-Language", "ko-KR")
            .build();
    }

    @PostConstruct
    public void initWebClient() {
        this.webClient = this.webClient.mutate()
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
    }
    
    public Mono<String> getAuthentication(){
    	return webClient.get()
    			.uri("/authentication")
    			.retrieve()
    			.bodyToMono(String.class);
    }
   

    public Mono<String> getTrendingMovies() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/trending/movie/day")
                .queryParam("api_key", token)
                .queryParam("language", "ko-KR")
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }

    public Mono<Map> getMovieDetails(String movieId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/movie/{movieId}")
                .queryParam("api_key", token)
                .queryParam("language","ko-KR")
                .build(movieId))
            .retrieve()
            .bodyToMono(Map.class);
    }
    
    public Mono<ActorDetailDTO> getActorDetails(int actorId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/person/{id}")
                .queryParam("append_to_response", "credits")
                .queryParam("language", "ko-KR")
                .build(actorId))
            .retrieve()
            .bodyToMono(ActorDetailDTO.class);
    }

//    public Mono<Map<String, Object>> searchTmdbMovie(String movieNm) {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/search/movie")
//                        .queryParam("api_key", token)
//                        .queryParam("query", movieNm)
//                        .queryParam("language", "ko-KR")
//                        .build())
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
//                .flatMap(response -> {
//                    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
//                    return results != null && !results.isEmpty() ? 
//                        Mono.just(results.get(0)) : 
//                        Mono.empty();
//                })
//                .onErrorResume(e -> {
//                    log.error("Error searching TMDB movie: ", e);
//                    return Mono.empty();
//                });
//    }
//    
    public Mono<MovieEntity> searchAndConvertMovie(String title) {
        return searchMovie(title)
                .map(this::convertToMovieEntity);
    }

    public Mono<MovieSearchResponse> searchMovie(String title) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", title)
                        .queryParam("language", "ko-KR")
                        .build())
                .retrieve()
                .bodyToMono(MovieSearchResponse.class);
    }

    private MovieEntity convertToMovieEntity(MovieSearchResponse response) {
        MovieEntity movieEntity = new MovieEntity();

        if (response.getResults() != null && !response.getResults().isEmpty()) {
            Movie movie = response.getResults().get(0);

            movieEntity.setMovieNm(movie.getTitle());
            movieEntity.setPosterPath(movie.getPosterPath());
            movieEntity.setOverview(movie.getOverview());
            movieEntity.setOpenDt(movie.getReleaseDate());

            // Set other fields that are available in MovieEntity but not in the DTO
            movieEntity.setMovieCd(String.valueOf(movie.getId())); // Using ID as movie code
            movieEntity.setMovieNmEn(""); // Not available in the DTO
            movieEntity.setPrdtYear(movie.getReleaseDate() != null ? movie.getReleaseDate().substring(0, 4) : "");

            // Initialize empty lists for fields that are not populated
            movieEntity.setNations(new ArrayList<>());
            movieEntity.setCompanys(new ArrayList<>());
            movieEntity.setGenres(new ArrayList<>());
            movieEntity.setActors(new ArrayList<>());
            movieEntity.setDirectors(new ArrayList<>());
        }

        return movieEntity;
    }
}


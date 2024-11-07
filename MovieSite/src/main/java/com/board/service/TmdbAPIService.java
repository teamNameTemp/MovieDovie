package com.board.service;


import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.board.dto.movie.ActorDetailDTO;
import com.board.dto.movie.DirectorDetailDTO;
import com.board.dto.movie.TmdbMovieDTO.Movie;
import com.board.dto.movie.TmdbMovieDTO.MovieSearchResponse;
import com.board.dto.movie.TmdbResponse.SearchResponse;
import com.board.entity.movie.MovieEntity;
import com.board.execption.ActorNotFoundException;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
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

    //배우 검색하기
    public Mono<ActorDetailDTO> getActorDetailsByName(String actorName) {
        return searchActor(actorName)
                .flatMap(this::getActorDetails);
    }

    private Mono<Integer> searchActor(String actorName) {
        return searchActorWithLanguage(actorName, "ko-KR")
                .onErrorResume(ActorNotFoundException.class, e -> 
                	searchActorWithLanguage(actorName, "en-US"));
    }

    private Mono<Integer> searchActorWithLanguage(String actorName, String language) {
    	return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/person")
                        .queryParam("api_key", token)
                        .queryParam("query", actorName)
                        .queryParam("language", language)
                        .build())
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .flatMap(response -> {
                    if (response.getResults() != null && !response.getResults().isEmpty()) {
                        return Mono.just(response.getResults().get(0).getId());
                    } else {
                    	log.info("언어: " + language +" 이름 : " + actorName);
                    	log.info("배우를 찾을 수 없습니다: " + actorName);
                    	return Mono.just(0);
//                        return Mono.error(new ActorNotFoundException("배우를 찾을 수 없습니다: " + actorName));
                    }
                });
    }

    private Mono<ActorDetailDTO> getActorDetails(Integer actorId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/person/{id}")
                        .queryParam("api_key", token)
                        .queryParam("language", "ko-KR")
                        .build(actorId))
                .retrieve()
                .bodyToMono(ActorDetailDTO.class);
    }
    //================================================================
    // 감독 검색
    public Mono<DirectorDetailDTO> getDirectorDetailsByName(String directorName) {
        return searchDirector(directorName)
                .flatMap(this::getDirectorDetails);
    }

    private Mono<Integer> searchDirector(String directorName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/person")
                        .queryParam("api_key", token)
                        .queryParam("query", directorName)
                        .queryParam("language", "ko-KR")
                        .build())
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .flatMap(response -> Flux.fromIterable(response.getResults())
                        .filter(person -> "Directing".equals(person.getKnownForDepartment()))
                        .next()
                        .map(person->person.getId()));
    }

    private Mono<DirectorDetailDTO> getDirectorDetails(Integer directorId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/person/{id}")
                        .queryParam("api_key", token)
                        .queryParam("language", "ko-KR")
                        .build(directorId))
                .retrieve()
                .bodyToMono(DirectorDetailDTO.class);
    }
    //================================================================
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


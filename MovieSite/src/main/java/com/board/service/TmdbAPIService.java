package com.board.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.board.dto.movie.ActorDetailDTO;

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

    public Mono<Map<String, Object>> searchTmdbMovie(String movieNm, String year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("api_key", token)
                        .queryParam("query", movieNm)
                        .queryParam("year", year)
                        .queryParam("language", "ko-KR")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(response -> {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                    return results != null && !results.isEmpty() ? 
                        Mono.just(results.get(0)) : 
                        Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Error searching TMDB movie: ", e);
                    return Mono.empty();
                });
    }

}
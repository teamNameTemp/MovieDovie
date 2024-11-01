package com.board.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.board.dto.movie.ActorDetailDTO;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class TmdbAPIService {
	@Value("${tmdb.api.base-url}")
	private String baseUrl;
	
	@Value("${tmdb.api.token}")
	private String token;
	
    private WebClient webClient;
    public TmdbAPIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
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
   

    public Mono<Map> getTrendingMovies() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/trending/movie/day")
                .queryParam("api_key", token)
                .build())
            .retrieve()
            .bodyToMono(Map.class);
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
}
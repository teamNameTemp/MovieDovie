package com.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KobisAPIService {
    @Value("${kobis.api.key}")
    private String apiKey;

    @Value("${kobis.api.base-url}")
    private String baseUrl;

    private final WebClient webClient;

    public KobisAPIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<String> getDailyBoxOffice(String targetDate) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boxoffice/searchDailyBoxOfficeList.json")
                        .queryParam("key", apiKey)
                        .queryParam("targetDt", targetDate)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getMovieInfo(String movieCd) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/searchMovieInfo.json")
                        .queryParam("key", apiKey)
                        .queryParam("movieCd", movieCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getActorInfo(String peopleCd) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/people/searchPeopleInfo.json")
                        .queryParam("key", apiKey)
                        .queryParam("peopleCd", peopleCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

}
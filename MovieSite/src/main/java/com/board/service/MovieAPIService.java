package com.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MovieAPIService {
// 
//    @Value("${kobis.api.key}")
//    private String kobisApiKey;
//
//    @Value("${tmdb.api.key}")
//    private String tmdbApiKey;
//
//    private final WebClient webClient;
//
//    public MovieAPIService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.build();
//    }
//
//    public Mono<Map> getMovieDetails(String movieName) {
//        return getKobisMovieInfo(movieName)
//                .flatMap(this::getTmdbMovieDetails);
//    }
//
//    private Mono<Map> getKobisMovieInfo(String movieName) {
//        String kobisUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json" +
//                "?key=" + kobisApiKey +
//                "&movieNm=" + movieName;
//
//        return webClient.get()
//                .uri(kobisUrl)
//                .retrieve()
//                .bodyToMono(Map.class)
//                .map(response -> {
//                    Map movieListResult = (Map) response.get("movieListResult");
//                    Map movieInfo = (Map) ((List) movieListResult.get("movieList")).get(0);
//                    return Map.of(
//                            "movieNm", movieInfo.get("movieNm"),
//                            "prdtYear", movieInfo.get("prdtYear")
//                    );
//                });
//    }
//
//    private Mono<Map> getTmdbMovieDetails(Map kobisInfo) {
//        String movieName = (String) kobisInfo.get("movieNm");
//        String year = (String) kobisInfo.get("prdtYear");
//        String tmdbUrl = "https://api.themoviedb.org/3/search/movie" +
//                "?api_key=" + tmdbApiKey +
//                "&query=" + movieName +
//                "&year=" + year;
//
//        return webClient.get()
//                .uri(tmdbUrl)
//                .retrieve()
//                .bodyToMono(Map.class)
//                .flatMap(searchResult -> {
//                    List results = (List) searchResult.get("results");
//                    if (results.isEmpty()) {
//                        return Mono.empty();
//                    }
//                    Map movie = (Map) results.get(0);
//                    int movieId = (int) movie.get("id");
//                    return getMovieDetailsById(movieId);
//                });
//    }
//
//    private Mono<Map> getMovieDetailsById(int movieId) {
//        String detailUrl = "https://api.themoviedb.org/3/movie/" + movieId +
//                "?api_key=" + tmdbApiKey +
//                "&language=ko-KR";
//
//        return webClient.get()
//                .uri(detailUrl)
//                .retrieve()
//                .bodyToMono(Map.class);
//    }
}
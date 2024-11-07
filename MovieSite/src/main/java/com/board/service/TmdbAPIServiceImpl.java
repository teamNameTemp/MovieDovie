package com.board.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.board.dto.movie.MovieDTO;
import com.board.dto.movie.TmdbResponse;
import com.board.entity.movie.ActorEntity;
import com.board.entity.movie.CompanyEntity;
import com.board.entity.movie.CountryEntity;
import com.board.entity.movie.CrewEntity;
import com.board.entity.movie.GenreEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.movie.SpokenLanguageEntity;
import com.board.entity.movie.VideoEntity;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class TmdbAPIServiceImpl implements TmdbAPIService {
	@Value("${tmdb.api.base-url}")
	private String baseUrl;
	
	@Value("${tmdb.api.token}")
	private String token;
	
    private WebClient webClient;
    public TmdbAPIServiceImpl(WebClient.Builder webClientBuilder) {
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
    //==============초반 설정 ^^^^^^^^^^^^^^^^^^^^^^^===================
    
    //최신 유행 영화 가져오기
    //timeWindow : week/day
    //page : 결과의 페이지번호(페이당 20개)
    //language : 결과의 언어
    public Mono<List<MovieDTO>> getTrendingMovies(String timeWindow, int page, String language){
    	 return webClient.get()
    			 .uri(uriBuilder -> uriBuilder
    					 .path("/trending/movie/{timeWindow}")
    					 .queryParam("api_key",token)
    					 .queryParam("page", page)
    					 .queryParam("language",language)
    					 .build(timeWindow))
    			 .retrieve()
    			 .bodyToMono(TmdbResponse.class)
    			 .map(TmdbResponse::getResults)
    			 .onErrorResume(e -> {
    			     log.error("Error fetching trending movies", e);
    			     return Mono.empty();
    			 });    	
    }
    
	//sort_by: 정렬 기준 (예: popularity.desc, vote_average.desc)
	//with_genres: 특정 장르 필터링
	//primary_release_year: 특정 연도의 영화만 필터링
    //vote_average.gte: 최소 평점 설정
    public Mono<MovieDTO> getDiscoverMovies(String sortBy, String withGenres, int page, String language) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("api_key", token)
                        .queryParam("sort_by", sortBy)
                        .queryParam("with_genres", withGenres)
                        .queryParam("page", page)
                        .queryParam("language", language)
                        .build())
                .retrieve()
                .bodyToMono(MovieDTO.class);
    }
    
    public Mono<MovieDTO> getMovieDetails(int movieId,String language) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{movieId}")
                        .queryParam("api_key", token)
                        .queryParam("language", language)
                        .queryParam("append_to_response", "credits,videos,release_dates")
                        .build(movieId))
                .retrieve()
                .bodyToMono(MovieDTO.class);
    }

    //================================================
    public Mono<MovieEntity> getCovertToEntity(Mono<MovieDTO> dto) {
        return dto.map(this::convertDtoToEntity);
    }

    private MovieEntity convertDtoToEntity(MovieDTO dto) {
        return MovieEntity.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .originalTitle(dto.getOriginalTitle())
            .tagline(dto.getTagline())
            .overview(dto.getOverview())
            .runtime(dto.getRuntime())
            .backdropPath(dto.getBackdropPath())
            .posterPath(dto.getPosterPath())
            .certification(dto.getReleaseDates().getResults().get(0).getReleaseDates().get(0).getCertification())
            .video(dto.getVideo())
            .originalLanguage(dto.getOriginalLanguage())
            .imdbId(dto.getImdbId())
            .popularity(dto.getPopularity())
            .releaseDate(dto.getReleaseDate())
            .status(dto.getStatus())
            .voteAverage(dto.getVoteAverage())
            .voteCount(dto.getVoteCount())
            .genres(convertGenres(dto.getGenres()))
            .videos(convertVideos(dto.getVideos()))
            .companys(convertCompanies(dto.getProductionCompanies()))
            .countrys(convertCountries(dto.getProductionCountries()))
            .spokenLanguages(convertSpokenLanguages(dto.getSpokenLanguages()))
            .actors(convertActors(dto.getCredits().getCast()))
            .crews(convertCrews(dto.getCredits().getCrew()))
            .build();
    }

    private List<GenreEntity> convertGenres(List<MovieDTO.Genre> genres) {
        if (genres == null) return new ArrayList<>();
        return genres.stream()
            .map(genre -> GenreEntity.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build())
            .collect(Collectors.toList());
    }

    private List<VideoEntity> convertVideos(MovieDTO.Videos videos) {
        if (videos == null || videos.getResults() == null) return new ArrayList<>();
        return videos.getResults().stream()
            .map(video -> VideoEntity.builder()
                .id(video.getId())
                .iso639_1(video.getIso639_1())
                .iso3166_1(video.getIso3166_1())
                .key(video.getKey())
                .name(video.getName())
                .site(video.getSite())
                .size(video.getSize())
                .type(video.getType())
                .build())
            .collect(Collectors.toList());
    }

    private List<CompanyEntity> convertCompanies(List<MovieDTO.ProductionCompany> companies) {
        if (companies == null) return new ArrayList<>();
        return companies.stream()
            .map(company -> CompanyEntity.builder()
                .id(company.getId())
                .logoPath(company.getLogoPath())
                .name(company.getName())
                .originCountry(company.getOriginCountry())
                .build())
            .collect(Collectors.toList());
    }

    private List<CountryEntity> convertCountries(List<MovieDTO.ProductionCountry> countries) {
        if (countries == null) return new ArrayList<>();
        return countries.stream()
            .map(country -> CountryEntity.builder()
                .iso3166_1(country.getIso3166_1())
                .name(country.getName())
                .build())
            .collect(Collectors.toList());
    }

    private List<SpokenLanguageEntity> convertSpokenLanguages(List<MovieDTO.SpokenLanguage> languages) {
        if (languages == null) return new ArrayList<>();
        return languages.stream()
            .map(language -> SpokenLanguageEntity.builder()
                .englishName(language.getEnglishName())
                .iso639_1(language.getIso639_1())
                .name(language.getName())
                .build())
            .collect(Collectors.toList());
    }

    private List<ActorEntity> convertActors(List<MovieDTO.Cast> cast) {
        if (cast == null) return new ArrayList<>();
        return cast.stream()
            .map(actor -> ActorEntity.builder()
                .id(actor.getId())
                .name(actor.getName())
                .characterName(actor.getCharacterName())
                .profilePath(actor.getProfilePath())
                .order(actor.getOrder())
                .build())
            .collect(Collectors.toList());
    }

    private List<CrewEntity> convertCrews(List<MovieDTO.Crew> crew) {
        if (crew == null) return new ArrayList<>();
        return crew.stream()
            .map(crewMember -> CrewEntity.builder()
                .id(crewMember.getId())
                .name(crewMember.getName())
                .jobTitle(crewMember.getJobTitle())
                .department(crewMember.getDepartment())
                .profilePath(crewMember.getProfilePath())
                .build())
            .collect(Collectors.toList());
    }
}

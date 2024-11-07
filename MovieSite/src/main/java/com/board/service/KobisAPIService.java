// package com.board.service;

// import java.util.Map;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;

// import com.board.dto.movie.KobisMovieDTO;
// import com.board.entity.movie.ActorEntity;
// import com.board.entity.movie.CompanyEntity;
// import com.board.entity.movie.DirectorEntity;
// import com.board.entity.movie.GenreEntity;
// import com.board.entity.movie.MovieEntity;
// import com.board.entity.movie.NationEntity;

// import lombok.extern.log4j.Log4j2;
// import reactor.core.publisher.Mono;

// @Service
// @Log4j2
// public class KobisAPIService {
//     @Value("${kobis.api.key}")
//     private String apiKey;

//     @Value("${kobis.api.base-url")
//     private String baseUrl;

//     private final WebClient webClient;
//     private final TmdbAPIService tmdb;

//     public KobisAPIService(WebClient.Builder webClientBuilder,TmdbAPIService tmdb) {
//         this.webClient = webClientBuilder.baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest").build();
//         this.tmdb = tmdb;
//     }

//     public Mono<String> getDailyBoxOffice(String targetDate) {
//         return webClient.get()
//                 .uri(uriBuilder -> uriBuilder
//                         .path("/boxoffice/searchDailyBoxOfficeList.json")
//                         .queryParam("key", apiKey)
//                         .queryParam("targetDt", targetDate)
//                         .build())
//                 .retrieve()
//                 .bodyToMono(String.class);
//     }

// //    public Mono<MovieEntity> getMovieInfo(String movieCd) {
// //        return webClient.get()
// //                .uri(uriBuilder -> uriBuilder
// //                        .path("/movie/searchMovieInfo.json")
// //                        .queryParam("key", apiKey)
// //                        .queryParam("movieCd", "20197803")
// //                        .build())
// //                .retrieve()
// //                .bodyToMono(MovieEntity.class);
// //    }

//     //영화이름으로 영화상세정보 가져오기
//     public Mono<MovieEntity> getMovieInfo(String movieCd) {
//         return webClient.get()
//                 .uri(uriBuilder -> uriBuilder
//                         .path("/movie/searchMovieInfo.json")
//                         .queryParam("key", apiKey)
//                         .queryParam("movieCd", movieCd)
//                         .build())
//                 .retrieve()
//                 .bodyToMono(KobisMovieDTO.class)
//                 .map(this::convertToMovieEntity)
//                 .onErrorResume(e -> {
//                     log.error("Error fetching movie info: ", e);
//                     return Mono.empty();
//                 });
//     }

//     private MovieEntity convertToMovieEntity(KobisMovieDTO response) {
//     	KobisMovieDTO.MovieInfo movieInfo = response.getMovieInfoResult().getMovieInfo();
//         MovieEntity entity = new MovieEntity();
        
//         // 기본 정보 설정
//         entity.setMovieCd(movieInfo.getMovieCd());
//         entity.setMovieNm(movieInfo.getMovieNm());
//         entity.setMovieNmEn(movieInfo.getMovieNmEn());
//         entity.setPrdtYear(movieInfo.getPrdtYear());
//         entity.setOpenDt(movieInfo.getOpenDt());
//         entity.setShowtime(movieInfo.getShowTm());

//         // 국가 정보 설정
//         if (movieInfo.getNations() != null) {
//             for (KobisMovieDTO.MovieInfo.Nation nation : movieInfo.getNations()) {
//                 NationEntity nationEntity = new NationEntity();
//                 nationEntity.setNationNm(nation.getNationNm());
//                 entity.addNations(nationEntity);
//             }
//         }

//         // 장르 정보 설정
//         if (movieInfo.getGenres() != null) {
//             for (KobisMovieDTO.MovieInfo.Genre genre : movieInfo.getGenres()) {
//                 GenreEntity genreEntity = new GenreEntity();
//                 genreEntity.setGenreNm(genre.getGenreNm());
//                 entity.addGenres(genreEntity);
//             }
//         }

//         // 감독 정보 설정
//         if (movieInfo.getDirectors() != null) {
//             for (KobisMovieDTO.MovieInfo.Director director : movieInfo.getDirectors()) {
//                 DirectorEntity directorEntity = new DirectorEntity();
//                 directorEntity.setPeopleNm(director.getPeopleNm());
//                 directorEntity.setPeopleNmEn(director.getPeopleNmEn());
//                 entity.addDirectors(directorEntity);
//             }
//         }

//         // 배우 정보 설정
//         if (movieInfo.getActors() != null) {
//             for (KobisMovieDTO.MovieInfo.Actor actor : movieInfo.getActors()) {
//                 ActorEntity actorEntity = new ActorEntity();
//                 actorEntity.setPeopleNm(actor.getPeopleNm());
//                 actorEntity.setPeopleNmEn(actor.getPeopleNmEn());
//                 actorEntity.setCast(actor.getCast());
//                 actorEntity.setCastEn(actor.getCastEn());
//                 entity.addActors(actorEntity);
//             }
//         }

//         // 제작사 정보 설정
//         if (movieInfo.getCompanys() != null) {
//             for (KobisMovieDTO.MovieInfo.Company company : movieInfo.getCompanys()) {
//                 CompanyEntity companyEntity = new CompanyEntity();
//                 companyEntity.setCompanyCd(company.getCompanyCd());
//                 companyEntity.setCompanyNm(company.getCompanyNm());
//                 companyEntity.setCompanyNmEn(company.getCompanyNmEn());
//                 companyEntity.setCompanyPartNm(company.getCompanyPartNm());
//                 entity.addCompanys(companyEntity);
//             }
//         }

//         // 심의 정보 설정
//         if (movieInfo.getAudits() != null && !movieInfo.getAudits().isEmpty()) {
//             KobisMovieDTO.MovieInfo.Audit audit = movieInfo.getAudits().get(0);
//             entity.setWatchGradeNm(audit.getWatchGradeNm());
//         }

// //        MovieEntity tmdbInfo = tmdb.searchTmdbMovie(movieInfo.getMovieNm()).subscribe(null);
// //        log.info(tmdbInfo.getOverview());
// //        log.info(tmdbInfo.getPosterPath());
// //        // 기타 정보 설정
// //         entity.setOverview(tmdbInfo.getOverview());
// //         entity.setPosterPath(tmdbInfo.getPosterPath());
//         // entity.setImgPath(...);
//         // entity.setHitno(0); // 초기 조회수 설정
//         // entity.setSalesAcc(...);
//         // entity.setAudiAcc(...);
//         // entity.setUploadCheck(...);

//         return entity;
//     }
    
//     public Mono<String> getActorInfo(String peopleCd) {
//         return webClient.get()
//                 .uri(uriBuilder -> uriBuilder
//                         .path("/people/searchPeopleInfo.json")
//                         .queryParam("key", apiKey)
//                         .queryParam("peopleCd", peopleCd)
//                         .build())
//                 .retrieve()
//                 .bodyToMono(String.class);
//     }
    
//     public Mono<String> getMoviesByGenre(String genreCd){
//     	return webClient.get()
//     			.uri(uriBuilder -> uriBuilder
//     					.path("/movie/searchMovieList.json")
//     					.queryParam("key", apiKey)
//     					.queryParam("genreAlt",genreCd)
//     					.build())
//     			.retrieve()
//     			.bodyToMono(String.class);
//     }
// }
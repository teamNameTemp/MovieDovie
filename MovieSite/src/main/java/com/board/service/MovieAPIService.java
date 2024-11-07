package com.board.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.entity.movie.ActorEntity;
import com.board.entity.movie.CompanyEntity;
import com.board.entity.movie.DailyBoxOfficeEntity;
import com.board.entity.movie.DirectorEntity;
import com.board.entity.movie.GenreEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.movie.NationEntity;
import com.board.entity.repository.ActorRepository;
import com.board.entity.repository.CompanyRepository;
import com.board.entity.repository.DailyBoxOfficeRepository;
import com.board.entity.repository.DirectorRepository;
import com.board.entity.repository.GenreRepository;
import com.board.entity.repository.MovieRepository;
import com.board.entity.repository.NationRepository;
import com.board.util.convertToMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Log4j2
public class MovieAPIService {
	private final TmdbAPIService tmdb;
	private final DailyBoxOfficeRepository dailyRepository;
	private final MovieRepository movieRepository;
	private final NationRepository nationRepository;
	private final GenreRepository genreRepository;
	private final DirectorRepository directorRepository;
	private final CompanyRepository CompanyRepository;
	private final ActorRepository actorRepository;

	public List<MovieEntity> getDailyBoxOffice() {
		LocalDate dateObj = LocalDate.now().minusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String yesterday = dateObj.format(formatter);
		String posterOriginPath = "https://image.tmdb.org/t/p/original";

		try {
			Optional<DailyBoxOfficeEntity> dailyEntities = dailyRepository.findById(yesterday);
			
			if (dailyEntities.isPresent()) {
				// 데이터베이스에서 데이터를 찾은 경우
				return convertEntitiesToMaps(dailyEntities.get(), posterOriginPath);
			} else {
				// 데이터베이스에 데이터가 없는 경우, API 호출
				List<MovieEntity> dailyList = getDailyBoxOfficeList(yesterday);
				if (!dailyList.isEmpty()) {
					List<MovieEntity> enrichedList = enrichMovieDetails(dailyList, posterOriginPath);
					saveToDatabase(yesterday, enrichedList);
					return enrichedList;
				} else {
					log.info("No daily box office data found for date: " + yesterday);
					return Collections.emptyList();
				}
			}
		} catch (Exception e) {
			log.error("요청을 처리하는 동안 오류가 발생했습니다.", e);
			return Collections.emptyList();
		}
	}

	private List<MovieEntity> convertEntitiesToMaps(DailyBoxOfficeEntity daily, String posterOriginPath) {
	    List<MovieEntity> movieEntities = new ArrayList<>();
	    for (MovieEntity movie : daily.getMovies()) {
	        Map<String, Object> movieMap = new HashMap<>();
	        movieMap.put("rank", movie.getRank());
	        movieMap.put("audiAcc", movie.getAudiAcc());
	        movieMap.put("movieCd", movie.getMovieCd());
	        movieMap.put("hitno", movie.getHitno());
	        movieMap.put("movieNm", movie.getMovieNm());
	        movieMap.put("movieNmEn", movie.getMovieNmEn());
	        movieMap.put("imgPath", movie.getImgPath());
	        movieMap.put("prdtYear", movie.getPrdtYear());
	        movieMap.put("salesAcc", movie.getSalesAcc());
	        movieMap.put("uploadcheck", movie.getUploadCheck());
	        movieMap.put("actors", movie.getActors());
	        movieMap.put("director", movie.getDirectors());
	        movieMap.put("companys", movie.getCompanys());
	        movieMap.put("genres", movie.getGenres());
	        movieMap.put("nations", movie.getNations());
	        movieMap.put("openDt", movie.getOpenDt());
	        movieMap.put("showtime", movie.getShowtime());
	        movieMap.put("posterPath", posterOriginPath + movie.getPosterPath());
	        movieMap.put("overview", movie.getOverview());
	        
	        // 여기서 MovieEntity를 새로 생성하거나 기존 movie 객체를 수정해야 합니다.
	        // 예를 들어:
	        movie.setPosterPath(posterOriginPath + movie.getPosterPath());
	        movieEntities.add(movie);
	    }
	    return movieEntities;
	}
	
	

	
	private List<MovieEntity> enrichMovieDetails(List<MovieEntity> dailyList, String posterOriginPath) throws JsonMappingException, JsonProcessingException {
		List<MovieEntity> movieDetailsList = new ArrayList<>();
		for (MovieEntity movie : dailyList) {
			String movieCd = movie.getMovieCd();
			String movieNm = movie.getMovieNm();
			//영화 상세정보
			MovieEntity info = getMovieInfo(movieCd).block();
			//영화 포스터이미지, 영화줄거리
			MovieEntity tmdbInfo = tmdb.searchAndConvertMovie(movieNm).block();
			
			movie.setPrdtYear(info.getPrdtYear());
			movie.setOpenDt(info.getOpenDt());
			movie.setShowtime(info.getShowtime());
			movie.setWatchGradeNm(info.getWatchGradeNm());
			movie.setOverview(tmdbInfo.getOverview());
			movie.setPosterPath(posterOriginPath + tmdbInfo.getPosterPath());
			movie.setNations(info.getNations());
			movie.setGenres(info.getGenres());
			movie.setCompanys(info.getCompanys());
			movie.setDirectors(info.getDirectors());
			movie.setActors(info.getActors());
			
			
			movieDetailsList.add(movie);
		}
		return movieDetailsList;
	}

	//DB세이브
//	private void saveToDatabase(String date, List<MovieEntity> movieList) {
//		DailyBoxOfficeEntity dailyEntity = new DailyBoxOfficeEntity();
//		dailyEntity.setDate(date);
//
//		log.info("Savelist = " + movieList);
//
//		List<MovieEntity> movieEntities = movieList.stream().map(movie -> {
//		    MovieEntity entity = new MovieEntity();
//		    entity.setRank(movie.getRank());
//		    entity.setMovieCd(movie.getMovieCd());
//		    entity.setMovieNm(movie.getMovieNm());
//		    entity.setMovieNmEn(movie.getMovieNmEn());
//		    entity.setOpenDt(movie.getOpenDt());
//		    entity.setShowtime(movie.getShowtime());
//		    entity.setPosterPath(movie.getPosterPath());
//		    entity.setOverview(movie.getOverview());
//		    entity.setPrdtYear(movie.getPrdtYear());
//		    entity.setAudiAcc(movie.getAudiAcc());
//		    entity.setHitno(movie.getHitno());
//		    entity.setImgPath(movie.getImgPath());
//		    entity.setSalesAcc(movie.getSalesAcc());
//		    entity.setUploadCheck(movie.getUploadCheck());
//		    return entity;
//		}).collect(Collectors.toList());
//
//		dailyEntity.setMovies(movieEntities);
//
//		try {
//			dailyRepository.save(dailyEntity);
//			log.info("Saved daily box office data for date: " + date);
//		} catch (Exception e) {
//			log.error("Error saving daily box office data to database", e);
//		}
//	}
	
	@Transactional
	private void saveToDatabase(String date, List<MovieEntity> movieList) {
	    DailyBoxOfficeEntity dailyEntity = dailyRepository.findById(date)
	            .orElseGet(() -> {
	                DailyBoxOfficeEntity newEntity = new DailyBoxOfficeEntity();
	                newEntity.setDate(date);
	                return newEntity;
	            });

	    log.info("Savelist = " + movieList);

	    List<MovieEntity> updatedMovies = movieList.stream().map(movie -> {
	        MovieEntity existingMovie = movieRepository.findByMovieNm(movie.getMovieNm())
	                .orElseGet(MovieEntity::new);

	        // Update existing movie or set fields for new movie
	        existingMovie.setRank(movie.getRank());
	        existingMovie.setMovieCd(movie.getMovieCd());
	        existingMovie.setMovieNm(movie.getMovieNm());
	        existingMovie.setMovieNmEn(movie.getMovieNmEn());
	        existingMovie.setOpenDt(movie.getOpenDt());
	        existingMovie.setShowtime(movie.getShowtime());
	        existingMovie.setPosterPath(movie.getPosterPath());
	        existingMovie.setOverview(movie.getOverview());
	        existingMovie.setPrdtYear(movie.getPrdtYear());
	        existingMovie.setAudiAcc(movie.getAudiAcc());
	        existingMovie.setHitno(movie.getHitno());
	        existingMovie.setImgPath(movie.getImgPath());
	        existingMovie.setSalesAcc(movie.getSalesAcc());
	        existingMovie.setUploadCheck(movie.getUploadCheck());

	        movieRepository.save(existingMovie);
	        
	        return existingMovie;
	    }).collect(Collectors.toList());

	    dailyEntity.setMovies(updatedMovies);

	    try {
	        dailyRepository.save(dailyEntity);
	        log.info("Saved/Updated daily box office data for date: " + date);
	    } catch (Exception e) {
	        log.error("Error saving/updating daily box office data to database", e);
	    }
	}

	private List<MovieEntity> getDailyBoxOfficeList(String targetDate) throws JsonProcessingException {
		Mono<String> result = kobis.getDailyBoxOffice(targetDate);
		return convertToMapper.getDailyBoxOffice(result.block());
	}
	
	public MovieEntity getMovieByCode(Long seqno) {
		return movieRepository.findById(seqno).get();
    }

	public Mono<MovieEntity> getMovieInfo(String movieNm) throws JsonMappingException, JsonProcessingException {
		return kobis.getMovieInfo(movieNm).flatMap(data -> Mono.fromCallable(()->{
			MovieEntity savedMovie = movieRepository.save(data);
			
			// Nation 엔티티 저장
			for (NationEntity nation : data.getNations()) {
				nation.setMovie(savedMovie);
				nationRepository.save(nation);
			}
			
			// Genre 엔티티 저장
			for (GenreEntity genre : data.getGenres()) {
				genre.setMovie(savedMovie);
				genreRepository.save(genre);
			}

			// Company 엔티티 저장
			for(CompanyEntity company : data.getCompanys()){
				company.setMovie(savedMovie);
				CompanyRepository.save(company);
			}
			
			// Director 엔티티 저장
			for(DirectorEntity director : data.getDirectors()){
				director.setMovie(savedMovie);
				directorRepository.save(director);
			}

			// Actor 엔티티 저장
			for(ActorEntity actor : data.getActors()){
				actor.setMovie(savedMovie);
				actorRepository.save(actor);
			}
			return savedMovie;
		}));
	}

}

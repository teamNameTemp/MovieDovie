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

import com.board.entity.DailyBoxOfficeEntity;
import com.board.entity.MovieEntity;
import com.board.entity.repository.DailyBoxOfficeRepository;
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
	private final KobisAPIService kobis;
	private final TmdbAPIService tmdb;
	private final DailyBoxOfficeRepository dailyRepository;

	public List<Map<String, Object>> getDailyBoxOffice() {
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
	            List<Map<String, Object>> dailyList = getDailyBoxOfficeList(yesterday);
	            if (!dailyList.isEmpty()) {
	                List<Map<String, Object>> enrichedList = enrichMovieDetails(dailyList, posterOriginPath);
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

	private List<Map<String, Object>> convertEntitiesToMaps(DailyBoxOfficeEntity daily, String posterOriginPath) {
	    List<Map<String, Object>> movieDetailsList = new ArrayList<>();
	    for (MovieEntity movie : daily.getMovies()) {
	        Map<String, Object> movieMap = new HashMap<>();
	        movieMap.put("rank", movie.getRank());
	        movieMap.put("movieCd", movie.getMovieCd());
	        movieMap.put("movieNm", movie.getMovieNm());
	        movieMap.put("prdtYear", movie.getOpenDt());
	        movieMap.put("showtime", movie.getShowtime());
	        movieMap.put("posterPath", posterOriginPath + movie.getPosterPath());
	        movieMap.put("overview", movie.getOverview());
	        movieMap.put("tmdbId", movie.getTmdbId());
	        movieDetailsList.add(movieMap);
	    }
	    return movieDetailsList;
	}

	private List<Map<String, Object>> enrichMovieDetails(List<Map<String, Object>> dailyList, String posterOriginPath) {
	    List<Map<String, Object>> movieDetailsList = new ArrayList<>();
	    for (Map<String, Object> movie : dailyList) {
	        String movieNm = (String) movie.get("movieNm");
	        String prdtYear = (String) movie.get("prdtYear");
	        System.out.println("Movie: " + movieNm + ", Year: " + prdtYear);

	        try {
	            Map<String, Object> tmdbInfo = tmdb.searchTmdbMovie(movieNm, prdtYear).block();
	            if (tmdbInfo != null) {
	                movie.put("posterPath", posterOriginPath + tmdbInfo.get("poster_path"));
	                movie.put("overview", tmdbInfo.get("overview"));
	                movie.put("tmdbId", tmdbInfo.get("id"));
	                System.out.println("Updated movie info: " + movie);
	            }
	            
				movie.put("showTm", getMovieInfo((String) movie.get("movieCd")).get("showTm"));
				log.info(getMovieInfo((String)movie.get("movieCd")));
	        } catch (Exception e) {
	            log.error("Error fetching TMDB info for movie: " + movieNm, e);
	        }
	        movieDetailsList.add(movie);
	    }
	    return movieDetailsList;
	}

	private void saveToDatabase(String date, List<Map<String, Object>> movieList) {
	    DailyBoxOfficeEntity dailyEntity = new DailyBoxOfficeEntity();
	    dailyEntity.setDate(date);

	    List<MovieEntity> movieEntities = movieList.stream().map(movie -> {
	    	MovieEntity entity = new MovieEntity();
	        entity.setRank((String) movie.get("rank"));
	        entity.setMovieCd((String) movie.get("movieCd"));
	        entity.setMovieNm((String) movie.get("movieNm"));
	        entity.setOpenDt((String) movie.get("openDt"));
	        entity.setShowtime((String)movie.get("showTm"));
	        entity.setPosterPath((String) movie.get("posterPath"));
	        entity.setOverview((String) movie.get("overview"));
	        entity.setTmdbId((Integer) movie.get("tmdbId"));
	        return entity;
	    }).collect(Collectors.toList());

	    dailyEntity.setMovies(movieEntities);

	    try {
	        dailyRepository.save(dailyEntity);
	        log.info("Saved daily box office data for date: " + date);
	    } catch (Exception e) {
	        log.error("Error saving daily box office data to database", e);
	    }
	}

	private List<Map<String, Object>> getDailyBoxOfficeList(String targetDate) throws JsonProcessingException {
		Mono<String> result = kobis.getDailyBoxOffice(targetDate);
		return convertToMapper.getDailyBoxOffice(result.block());
	}
	
	private Map<String,Object> getMovieInfo(String movieCd) throws JsonMappingException, JsonProcessingException{
		Mono<String> result = kobis.getMovieInfo(movieCd);
		return convertToMapper.getMovieInfo(result.block());
	}

}

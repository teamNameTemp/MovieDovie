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

import com.board.entity.movie.DailyBoxOfficeEntity;
import com.board.entity.movie.MovieEntity;
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
	    	MovieEntity entity = MovieEntity.builder()
	    							.rank((String)movie.get("rank"))
	    							.movieCd((String)movie.get("movieCd"))
	    							.movieNm((String)movie.get("movieNm"))
	    							.movieNmEn((String)movie.get("movieNmEn"))
	    							.openDt((String)movie.get("openDt"))
	    							.showtime((String)movie.get("showtime"))
	    							.posterPath((String)movie.get("posterPath"))
	    							.overview((String)movie.get("overview"))
	    							.tmdbId((Integer)movie.get("tmdbId"))
	    							.prdtYear((String)movie.get("prdtYear"))
	    							.audiAcc((String)movie.get("audiAcc"))
	    							.hitno((Integer)movie.get("hitno"))
	    							.imgPath((String)movie.get("imgPath"))
	    							.salesAcc((String)movie.get("salesAcc"))
	    							.uploadCheck((String)movie.get("uploadcheck"))
	    							.actors((List)movie.get("actors"))
	    							.directors((List)movie.get("directors"))
	    							.companys((List)movie.get("companys"))
	    							.nations((List)movie.get("nations"))
	    							.genres((List)movie.get("genres"))
	    							.build();
	      
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

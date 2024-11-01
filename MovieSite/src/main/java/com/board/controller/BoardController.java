package com.board.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.dto.movie.ActorDetailDTO;
import com.board.service.KobisAPIService;
import com.board.service.TmdbAPIService;
import com.board.util.convertToMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final KobisAPIService kobis;
	private final TmdbAPIService tmdb;

	// 추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect() {
	}

	@GetMapping("/list")
	public void getMovieDetails(Model model, @RequestParam("targetDt") String targetDt) {
	    String posterOriginPath = "https://image.tmdb.org/t/p/original";
	    try {
	        List<Map<String, Object>> dailyList = getDailyBoxOfficeList(targetDt);
	        if (!dailyList.isEmpty()) {
	            List<Map<String, Object>> movieDetailsList = new ArrayList<>();

	            for (Map<String, Object> movie : dailyList) {
	                String movieNm = (String) movie.get("movieNm");
	                String prdtYear = (String) movie.get("prdtYear");
	                
	                // TMDB API를 사용하여 영화 정보 검색
	                Mono<Map<String, Object>> tmdbInfo = tmdb.searchTmdbMovie(movieNm, prdtYear);
	                tmdbInfo.subscribe(info -> {
	                    movie.put("posterPath", posterOriginPath + info.get("poster_path"));
	                    movie.put("overview", info.get("overview"));
	                    System.out.println(movie);
	                    movieDetailsList.add(movie);
	                });

	            }
	            model.addAttribute("dailyMovies", movieDetailsList);
	            
	        } else {
	            model.addAttribute("error", "영화 목록을 찾지 못했습니다.");
	        }
	    } catch (Exception e) {
	        // 예외 로깅
	        model.addAttribute("error", "요청을 처리하는 동안 오류가 발생했습니다.");
	    }
	}
	// 장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}

	@GetMapping("/movieDetail")
	public void getMovieDetail() {

	}

	@GetMapping("/tmdb")
	public void getAuthentication(Model model){
		 //tmdb.getAuthentication();
		Mono<Map> mono = tmdb.getMovieDetails("1034541");
		mono.subscribe(value -> System.out.println(value.get("poster_path")));
		System.out.println(mono.block());
		Mono<ActorDetailDTO> actors = tmdb.getActorDetails(287);
		System.out.println("배우 : " + actors.block());
		String posterOriginPath = "https://image.tmdb.org/t/p/original";

		model.addAttribute("actor",actors.block());
		model.addAttribute("detail",mono.block());
		model.addAttribute("image",posterOriginPath+mono.block().get("poster_path"));
		model.addAttribute("image",posterOriginPath+actors.block().getProfile_path());;
	}


	private List<Map<String, Object>> getDailyBoxOfficeList(String targetDate) throws JsonProcessingException {
		Mono<String> result = kobis.getDailyBoxOffice(targetDate);
		return convertToMapper.getDailyBoxOffice(result.block());
	}

//	private Map<String, Object> getMovieInfo(String movieCd) throws JsonProcessingException {
//		String movieInfoResult = kobis.getMovieInfo(movieCd);
//		return convertToMapper.getMovieInfo(movieInfoResult);
//	}
}

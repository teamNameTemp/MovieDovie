package com.board.controller;

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
	public void getlist() {
	}

	// 장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}

	@GetMapping("/movieDetail")
	public void getMovieDetail() {

	}

	@GetMapping("/movieDisplay")
	public void getMovieDisplay() {

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
	
	@GetMapping("/trending")
	public Mono<Map> getTrendingMovies() {
		return tmdb.getTrendingMovies();
	}

	@GetMapping("/{movieId}")
	public Mono<Map> getMovieDetails(@PathVariable String movieId) {
		return tmdb.getMovieDetails(movieId);
	}

//	@GetMapping("/daily")
//	public void getDaily(@RequestParam("targetDt") String targetDate, Model model) {
//		try {
//			List<Map<String, Object>> dailyList = getDailyBoxOfficeList(targetDate);
//			if (!dailyList.isEmpty()) {
//				String movieCd = (String) dailyList.get(0).get("movieCd");
//				Map<String, Object> movieInfo = getMovieInfo(movieCd);
//				System.out.println(movieInfo);
//				model.addAttribute("movies", dailyList);
//				model.addAttribute("movieInfo", movieInfo);
//			} else {
//				// 적절한 오류 처리
//				model.addAttribute("error", "영화 목록을 찾지 못했습니다.");
//			}
//		} catch (Exception e) {
//			// 예외 로깅
//			model.addAttribute("error", "An error occurred while processing your request");
//		}
//	}

//	private List<Map<String, Object>> getDailyBoxOfficeList(String targetDate) throws JsonProcessingException {
//		String result = kobis.getDailyBoxOffice(targetDate);
//		return convertToMapper.getDailyBoxOffice(result);
//	}
//
//	private Map<String, Object> getMovieInfo(String movieCd) throws JsonProcessingException {
//		String movieInfoResult = kobis.getMovieInfo(movieCd);
//		return convertToMapper.getMovieInfo(movieInfoResult);
//	}
}

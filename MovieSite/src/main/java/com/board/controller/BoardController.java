package com.board.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.dto.movie.ActorDetailDTO;
import com.board.service.KobisAPIService;
import com.board.service.MovieAPIService;
import com.board.service.TmdbAPIService;
import com.board.util.convertToMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final MovieAPIService service;
	
	// 추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect() {
	}

	@GetMapping("/list")
	public void getList(Model model) {
		model.addAttribute("dailyMovies",service.getDailyBoxOffice());
	
	}
	// 장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}

	@GetMapping("/movieDetail")
	public void getMovieDetail() {

	}

//	@GetMapping("/tmdb")
//	public void getAuthentication(Model model){
//		 //tmdb.getAuthentication();
//		Mono<Map> mono = tmdb.getMovieDetails("1034541");
//		mono.subscribe(value -> System.out.println(value.get("poster_path")));
//		System.out.println(mono.block());
//		Mono<ActorDetailDTO> actors = tmdb.getActorDetails(287);
//		System.out.println("배우 : " + actors.block());
//		String posterOriginPath = "https://image.tmdb.org/t/p/original";
//
//		model.addAttribute("actor",actors.block());
//		model.addAttribute("detail",mono.block());
//		model.addAttribute("image",posterOriginPath+mono.block().get("poster_path"));
//		model.addAttribute("image",posterOriginPath+actors.block().getProfile_path());;
//	}
	
//	@GetMapping("/daily")
//	public void getDaily(@RequestParam("targetDt") String targetDate, Model model) {
//		try {
//			List<Map<String, Object>> dailyList = getDailyBoxOfficeList(targetDate);
//			if (!dailyList.isEmpty()) {
//				String movieCd = (String) dailyList.get(0).get("movieCd");
//				Mono<String> movieInfo = kobis.getMovieInfo(movieCd);
//				Map<String,Object> info =  convertToMapper.getMovieInfo(movieInfo.block());
//				model.addAttribute("movies", dailyList);
//				model.addAttribute("movieInfo", info);
//			} else {
//				// 적절한 오류 처리
//				model.addAttribute("error", "영화 목록을 찾지 못했습니다.");
//			}
//		} catch (Exception e) {
//			// 예외 로깅
//			model.addAttribute("error", "An error occurred while processing your request");
//		}
//	}

	

//	private Map<String, Object> getMovieInfo(String movieCd) throws JsonProcessingException {
//		String movieInfoResult = kobis.getMovieInfo(movieCd);
//		return convertToMapper.getMovieInfo(movieInfoResult);
//	}
}

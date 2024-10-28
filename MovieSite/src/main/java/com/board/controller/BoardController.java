package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	//추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect() {}
	
	@GetMapping("/list")
	public void getlist() {}

	//장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}
}

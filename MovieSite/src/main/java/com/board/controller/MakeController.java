package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.board.service.MovieAPIServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MakeController {
	private final MovieAPIServiceImpl movie;
	
	@GetMapping("/make/movie")
	public void getMake() {
		
	}
}

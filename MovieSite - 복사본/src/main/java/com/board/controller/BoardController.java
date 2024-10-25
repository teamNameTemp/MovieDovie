package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
	@GetMapping("/movieSelect")
	public void getMovieSelect() {}
	
	@GetMapping("/list")
	public void getlist() {}

}

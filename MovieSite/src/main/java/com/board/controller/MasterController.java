package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/master")
public class MasterController {

	@GetMapping("/memberRequest")
	public void getMemberRequest() {
	}

	@GetMapping("/memberRequestView")
	public void getMemberRequestView() {
	}
	
	@GetMapping("/memberStats")
	public void getMemberStats() {
	}
	
	@GetMapping("/movieStats")
	public void getMovieStats() {
	}
	
	@GetMapping("/movieUpload")
	public void getMovieUpload() {
	}
}

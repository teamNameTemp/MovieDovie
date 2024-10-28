package com.board.controller;

import javax.swing.text.Document;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/member")
public class MemberController {
	@GetMapping("/login")
	public void getLogin() {
	}

	// 로그인 --> Spring Security에 의해 인터셉트
	@PostMapping("/login")
	public void postLogin() {
		
	}

	// 로그인 처리 --> Spring Security에 의한 로그인 진행 후 처리 할 부분
	@ResponseBody
	@PostMapping("/loginCheck")
	public String postLoginCheck() throws Exception {
		

		return "{\"message\":\"good\"}";
	}
	
	@GetMapping("/logout")
	public void getLogout() {}
	
	@GetMapping("/signup")
	public void getSignup() {}
	
}

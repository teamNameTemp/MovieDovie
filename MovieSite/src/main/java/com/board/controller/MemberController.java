package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.service.MemberService;

import lombok.AllArgsConstructor;






@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService service;
	
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
	
	//로그아웃
	@GetMapping("/logout")
	public void getLogout() {}
	
	//회원가입
	@GetMapping("/signup")
	public void getSignup() {}
	
	
	//회원가입 --> 로그인페이지(DTO필요)
	@ResponseBody
	@PostMapping("/signup")
	public Map<String,Object> postSignup() throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("message", "good");
		return map;
	}
	


	
	//이메일 찾기 화면 보기
	@GetMapping("/searchEmail")
	public void getSearchEmail() {}
	

	
	//비밀번호 찾기
	@GetMapping("/searchPassword")
	public void getSearchPassword() {}
	
}

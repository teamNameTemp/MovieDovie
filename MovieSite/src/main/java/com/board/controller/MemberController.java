 package com.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.FavGenreDTO;
import com.board.dto.MemberDTO;
import com.board.entity.repository.FavGenreRepository;
import com.board.service.FavGenreServiceImpl;
import com.board.service.MemberService;
import com.board.service.TmdbAPIServiceImpl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService service;
	private final BCryptPasswordEncoder pwdEncoder;
	private final TmdbAPIServiceImpl tmdb;
	private final FavGenreServiceImpl favGenre;
		
	//로그인 화면 보기
	@GetMapping("/login")
	public void getLogin() { }
	
	//로그인 --> Spring Security에 의해 인터셉트
	@PostMapping("/login")
	public void postLogin() {}
	
	//로그인 처리 --> Spring Security에 의한 로그인 진행 후 처리 할 부분
	@ResponseBody
	@PostMapping("/loginCheck")
	public String postLoginCheck(MemberDTO member,HttpSession session) {
		MemberDTO dto = service.memberInfo(member.getEmail());
		log.info(dto.getPassword());
		log.info(member.getPassword());
		
		//아이디 존재 여부 확인
		if(service.emailCheck(dto.getEmail()) == 0) {
			return "{\"message\":\"ID_NOT_FOUND\"}";
		}
		
		//패스워드가 올바르게 들어 왔는지 확인
		//log.info(member.getPassword() + " | " + service.memberInfo(member.getPassword()).getPassword());
		if(!pwdEncoder.matches(member.getPassword(), dto.getPassword())) {
			//잘못된 패스워드 일때
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}
			
		return "{\"message\":\"good\"}";
	
	}
	
	//로그아웃
	@GetMapping("/logout")
	public void getLogout() {		
		log.info("====================== 로그 아웃 ======================");
	}
	
	//로그아웃 전처리
	@GetMapping("/beforeLogout")
	public String getBeforeLogout(HttpSession session,Model model) {
		
		String email = (String)session.getAttribute("email");
		service.lastdateUpdate(email, "logout");
		service.memberLogRegistry(email, "logout");		
		session.invalidate(); 
		log.info("====================== 로그 아웃 전 로그 등록 및 세션 종료======================");
		return "redirect:/member/logout";
	}
	
	//회원 등록 화면 보기
	@GetMapping("/signup")
	public void getSignup() {	}
	
	//회원 가입 및 회원 일반 정보 수정
	@ResponseBody
	@PostMapping("/signup")
	public Map<String,Object> postSignup(MemberDTO member) throws Exception {
		Map<String,Object> map = new HashMap<>();
		member.setPassword(pwdEncoder.encode(member.getPassword()));
		service.signup(member);
		
		map.put("message", "good");
		map.put("username",member.getUsername());
		return map;
	}

		
	// 추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect(Model model) {
		Map<String,Integer> map = tmdb.getGenreList().block();
		
		model.addAttribute("genres",map);
	}
	
	@ResponseBody
	@PostMapping("/selectGenre")
	public Map<String,Boolean> postSelectGenres(@RequestBody Map<String, List<String>> payload, HttpSession session){
		List<String> selectedGenres = payload.get("genres");
		FavGenreDTO favGenreDTO = new FavGenreDTO();
		switch(selectedGenres.size()) {
		case 1:
			favGenreDTO.setGenre1(selectedGenres.get(0));
			break;
		case 2:
			favGenreDTO.setGenre2(selectedGenres.get(1));
			break;
		case 3:
			favGenreDTO.setGenre3(selectedGenres.get(2));
			break;
		}
		
		String email = (String)session.getAttribute("email");
		favGenreDTO.setEmail(email);

		favGenre.saveTheFavGenre(favGenreDTO);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("success", true);
		return response;
	}
		
		
	//OAuth2 가입
	@GetMapping("/board/oauth2Signup")
	public void getOAuth2Signup() throws Exception {
		
	}
	
	//회원 가입 시 아이디 중복 확인
	@ResponseBody
	@PostMapping("/emailCheck")
	public int postEmailCheck(@RequestBody String email) throws Exception{
		
		int result = service.emailCheck(email);
		return result;
	}
	
	//이메일 찾기 화면 보기
	@GetMapping("/searchEmail")
	public void getSearchEmail() {}
	
	//이메일 찾기
	@ResponseBody
	@PostMapping("/searchEmail")
	public String postSearchEmail(MemberDTO member) {
		
		String email = service.searchEmail(member) == null?"ID_NOT_FOUND":service.searchEmail(member);		
		return "{\"message\":\"" + email + "\"}";
	}
	
	//비밀번호 찾기 화면 보기
	@GetMapping("/searchPassword")
	public void getSearchPassword() {}
	
	//이메일 찾기
	@ResponseBody
	@PostMapping("/searchPassword")
	public String postSearchPassword(MemberDTO member) {
		
		String password = service.searchPassword(member) == null?"ID_NOT_FOUND":service.searchPassword(member);		
		return "{\"message\":\"" + password + "\"}";
	}
	
}

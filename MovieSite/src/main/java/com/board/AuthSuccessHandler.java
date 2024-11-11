package com.board;


import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.board.dto.MemberDTO;
import com.board.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	private final MemberService service;
	
	//로그인 성공 시 해야할 명령문
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//authentication.getName() //--> 로그인 시 입력된 userid 값을 가져옴.
		MemberDTO member = service.memberInfo(authentication.getName());
		
		//로그인 날짜 및 회원 로그 정보 등록
		
		service.lastdateUpdate(member.getEmail(), "login");
		service.memberLogRegistry(member.getEmail(), "login");
		 
		//세션 생성
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
		session.setAttribute("email", service.memberInfo(member.getEmail()).getEmail());
		session.setAttribute("username", service.memberInfo(member.getEmail()).getUsername());
		session.setAttribute("role", service.memberInfo(member.getEmail()).getRole());
		
		 
		log.info("------------------------ 로그인 성공 : 세션 생성 ------------------------ ");
				
		//패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우 ...
		//패스워드 변경 기한 도래 여부 확인
		/*
		 * LocalDateTime lastpwDate = member.getLastpwdate(); String reDate =
		 * lastpwDate.plusDays(30).toString(); String today =
		 * LocalDateTime.now().toString();
		 */
		
		//로그인 성공하면 넘어가는 페이지
		String url = "/board/selectGenre";

		/*
		 * if(reDate.compareTo(today) < 0) //패스워드 변경일이 넘었다... url =
		 * "/board/checkPasswordNotice";
		 */
		//log.info("------------------------ 로그인 성공 : 30일 패스워드 변경 기한 확인 ------------------------ ");
		
		setDefaultTargetUrl(url);
		super.onAuthenticationSuccess(request, response, authentication);
		
	}

}

package com.board;


import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.board.dto.MemberDTO;
import com.board.service.FavGenreService;
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
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService service;
    private final FavGenreService favGenre;

    // 로그인 성공 시 실행되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 로그인 사용자 정보 조회
        MemberDTO member = service.memberInfo(authentication.getName());

        // 로그인 날짜 및 회원 로그 정보 등록
        service.lastdateUpdate(member.getEmail(), "login");
//        service.memberLogRegistry(member.getEmail(), "login");

        // 세션 생성 및 설정
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600 * 24 * 7); // 세션 유지 기간 설정
        session.setAttribute("email", member.getEmail());
        session.setAttribute("username", member.getUsername());
        session.setAttribute("role", member.getRole());
        session.setAttribute("nickname", member.getNickname());

        log.info("------------------------ 로그인 성공 : 세션 생성 ------------------------ ");

        // 로그인 성공 후 리다이렉트할 URL 설정
        if(favGenre.getFavGenre() == null || favGenre.getFavGenre().isEmpty()){
            setDefaultTargetUrl("/member/selectGenre");
        }
        else{
            setDefaultTargetUrl("/board/list");
        }
        
        // 설정된 리다이렉트 URL로 이동
        super.onAuthenticationSuccess(request, response, authentication);
    }
}


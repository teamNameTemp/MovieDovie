package com.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.board.service.OAuth2UserDetailsServiceImpl;
import com.board.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@AllArgsConstructor
@EnableWebSecurity
@Configuration
@Log4j2
public class WebSecurityConfig {
	
	private final AuthSuccessHandler authSuccessHandler; 
	private final AuthFailureHandler authFailureHandler;
	private final UserDetailsServiceImpl userDetailsService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final OAuth2FailureHandler oAuth2FailureHandler;
	
	
	//스프링시큐리티에서 암호화 관련 객체를 가져다가 스프링빈으로 등록
	@Bean
	BCryptPasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder(); 
	}
	
	//스프링 시큐리티 적용 제외 대상 설정--> 스프링빈을 등록
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**","/css/**","/profile/**","/js/**");
	}
	
	//스프링시큐리티 로그인 화면 사용 비활성화, CSRF/CORS 공격 방어용 보안 설정 비활성화
	@Bean
	SecurityFilterChain filter(HttpSecurity http) throws Exception {
		
		//스프링 시큐리티의 FormLogin 설정 (커스터마이징)
		http
			.formLogin((login) -> login  //기본 로그인 페이지
				.usernameParameter("email") //사용자 정의 아이디 파라미터 이름 지정
				.loginPage("/member/login") //사용자 정의 로그인 페이지 경로 지정
				.successHandler(authSuccessHandler) //로그인 성공 시 동작 지정
				.failureHandler(authFailureHandler)); //로그인 실패 시 동작 지정
		//스프링 시큐리티의 자동로그인 설정
		http
			.rememberMe((me) -> me
				.key("movie")
				.alwaysRemember(false)
				.tokenValiditySeconds(3600*24*7)
				.rememberMeParameter("remember-me")
				.userDetailsService(userDetailsService) //사용자 인증 정보 확인
				.authenticationSuccessHandler(authSuccessHandler));

		
		//스프링 시큐리티의 접근권한 설정(Authentication)
		http
			.authorizeHttpRequests((authz)-> authz
					.requestMatchers("/member/**").permitAll()
					.requestMatchers("/board/**").permitAll()
					.requestMatchers("/master/**").permitAll()					
					.anyRequest().authenticated());
		//세션 설정
		http
			.sessionManagement(management -> management
				.maximumSessions(1) //하나의 아이디에 대한 다중 로그인 허용 개수
				.maxSessionsPreventsLogin(false) //다중 로그인 개수를 초과하였을 경우 처리 방법
				.expiredUrl("/member/login"));				
		
		//스프링 시큐리티의 로그 아웃
		http
			.logout(logout -> logout
					.logoutUrl("/member/logout")
					.logoutSuccessUrl("/member/login")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID","remember-me")
					.permitAll());
		
		//OAuth2 설정 
		http
			.oauth2Login((login) -> login
			.loginPage("/member/selectGenre")
			.successHandler(oAuth2SuccessHandler)
			.failureHandler(oAuth2FailureHandler));
		
		//CSRF, CORS 공격 방어를 위한 보안 설정 비활성화 --> 없으면 로그인 진행 안됨
		http
			.csrf((csrf) -> csrf.disable());
		http 
			.cors((cors) -> cors.disable());
		
		log.info("------------------------ 스프링 시큐리티 활성화 및 접근 제한 설정 완료------------------------");
		
		return http.build();
	}
	
}

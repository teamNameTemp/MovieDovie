package com.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
@Log4j2
public class WebSecurityConfig {

	private final AuthSuccessHandler authSuccessHandler;
	private final AuthFailureHandler authFailureHandler;
	// 스프링 시큐리티 적용 제외 대상 설정--> 스프링빈을 등록
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/css/**", "/js/**");
	}

	// 스프링시큐리티 로그인 화면 사용 비활성화, CSRF/CORS 공격 방어용 보안 설정 비활성화
	@Bean
	SecurityFilterChain filter(HttpSecurity http) throws Exception {
		 http
         .authorizeHttpRequests((authz) -> authz
             .anyRequest().permitAll()
         )
         .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
         .formLogin(form -> form
             .loginPage("/member/login")
             .permitAll()
             .successHandler(authSuccessHandler)
             .failureHandler(authFailureHandler)
         )
         .logout(logout -> logout
             .permitAll()
         );

     return http.build();
	}

}

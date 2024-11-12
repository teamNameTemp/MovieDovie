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
    
    @Bean
    BCryptPasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder(); 
    }
    
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/css/**", "/profile/**", "/js/**");
    }
    
    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {
        
        http
            .formLogin((login) -> login
                .usernameParameter("email")
                .loginPage("/member/login")
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler));
        
        http
            .rememberMe((me) -> me
                .key("movie")
                .alwaysRemember(false)
                .tokenValiditySeconds(3600 * 24 * 7)
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
                .authenticationSuccessHandler(authSuccessHandler));
        
        http
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/member/**").permitAll()
                .requestMatchers("/board/**").permitAll()
                .requestMatchers("/master/**").permitAll()
                .anyRequest().authenticated());
        
        http
            .sessionManagement(management -> management
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/member/login"));
        
        http
            .logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/member/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll());
        
        // OAuth2 설정에 리다이렉트 URL 추가
        http
            .oauth2Login((login) -> login
                .loginPage("/member/login")
                .defaultSuccessUrl("/board/selectGenre", true) // 로그인 성공 후 리다이렉트할 페이지 설정
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler));
        
        http
            .csrf((csrf) -> csrf.disable());
        http
            .cors((cors) -> cors.disable());
        
        log.info("------------------------ 스프링 시큐리티 활성화 및 접근 제한 설정 완료------------------------");
        
        return http.build();
    }
}

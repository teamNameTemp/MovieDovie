package com.board.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	// 원본
	// 구현코드 기술 및 필요한 라이브러리 및 소스 임포트
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		// 고정된 사용자 정보 생성
		return new User("defaultUser", // 사용자명
				"{noop}password", // 비밀번호 (평문 비밀번호를 위해 {noop} 접두사 사용)
				true, // 계정 활성화 여부
				true, // 계정 만료 여부
				true, // 자격 증명 만료 여부
				true, // 계정 잠금 여부
				authorities);
	}

	// private final MemberRepository memberRepository;

	/*
	 * @Override public UserDetails loadUserByUsername(String username) throws
	 * UsernameNotFoundException {
	 * 
	 * //username은 스프링 시큐리티가 필터로 작동하면서 로그인 요청에서 가로채온 userid임. MemberEntity
	 * memberInfo = memberRepository.findById(username).get();
	 * 
	 * if(memberInfo == null) { throw new
	 * UsernameNotFoundException("아이디가 존재하지 않습니다."); }
	 * 
	 * //SimpleGrantedAuthority : 여러개의 사용자 Role값을 받는 객체 List<SimpleGrantedAuthority>
	 * grantedAuthorities = new ArrayList<>(); SimpleGrantedAuthority
	 * grantedAuthority = new SimpleGrantedAuthority(memberInfo.getRole());
	 * grantedAuthorities.add(grantedAuthority);
	 * 
	 * User user = new User(username, memberInfo.getPassword(), grantedAuthorities);
	 * 
	 * return user; }
	 */

}

package com.board.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.MemberEntity;

//각 Repository에 extends JpaRepository<entity,반환 값(String)> 상속 받아서 사용할 것
public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	//쿼리문 구현
	//JPA 구문(ORM) , native sql 등
	
    // 이메일 중복 확인을 위한 메서드 (이메일이 존재하는지 확인)
    boolean existsByEmail(String email);

    // 닉네임 중복 확인을 위한 메서드 (닉네임이 존재하는지 확인)
    boolean existsByNickname(String nickname);
	
	public Optional<MemberEntity> findByUsernameAndTelno(String username, String telno);
	public Optional<MemberEntity> findByEmailAndUsername(String email, String username);

} 
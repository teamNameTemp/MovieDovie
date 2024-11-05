package com.board.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.MemberEntity;

//각 Repository에 extends JpaRepository<entity, ID(String)> 상속 받아서 사용할 것
public interface MemberRepository extends JpaRepository<MemberEntity, String>{

	Optional<MemberEntity> findByEmail(String email);
	//쿼리문 구현
	//JPA 구문(ORM) , native sql 등
	
	public Optional<MemberEntity> findByUsernameAndTelno(String username, String telno);
	public Optional<MemberEntity> findByEmailAndUsername(String email, String username);
}
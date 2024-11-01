package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.MemberEntity;

//각 Repository에 extends JpaRepository<entity, ID(String)> 상속 받아서 사용할 것
public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	//쿼리문 구현
	//JPA 구문(ORM) , native sql 등
}
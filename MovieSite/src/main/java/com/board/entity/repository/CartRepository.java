package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.CartEntity;
import com.board.entity.MemberEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    // 특정 회원의 장바구니 목록 조회
    List<CartEntity> findByMember(MemberEntity member);
    
    // 특정 회원과 영화로 장바구니 항목 조회
//    Optional<CartEntity> findByMemberAndMovie(MemberEntity member, MovieEntity movie);
}
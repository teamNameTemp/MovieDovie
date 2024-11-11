package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Long>{

}

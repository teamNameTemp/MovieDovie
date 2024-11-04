package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.MemberLogEntity;
import com.board.entity.MemberLogEntityID;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, MemberLogEntityID> {

	
}

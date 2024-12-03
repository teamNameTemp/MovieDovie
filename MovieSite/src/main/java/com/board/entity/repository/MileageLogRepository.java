package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.board.entity.MileageLogEntity;

public interface MileageLogRepository extends JpaRepository<MileageLogEntity, Long> {

	@Query("SELECT distinct m FROM MileageLogEntity m WHERE m.email = :email")
	List<MileageLogEntity> findAllByEmail(String email);

}

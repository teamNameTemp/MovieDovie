package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.board.entity.MemberLogEntity;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, Long>{


	//관리자페이지 - 통계 : 방문 수 확인
	@Query(value = "SELECT " +
	        "  SUM(CASE WHEN TRUNC(m.inouttime) = TRUNC(SYSDATE) - 1 THEN 1 ELSE 0 END) AS yesterdayVisitors, " +
	        "  SUM(CASE WHEN TRUNC(m.inouttime) = TRUNC(SYSDATE) THEN 1 ELSE 0 END) AS todayVisitors, " +
	        "  COUNT(m.seqno) AS totalVisitors " +
	        " FROM tbl_member_log m WHERE m.status = 'LOGIN'", nativeQuery = true)
	public List<Object[]> visitResult();
}
 
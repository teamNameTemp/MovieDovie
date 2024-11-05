package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.board.entity.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Long>{

	//각 영화에 대해 좋아요 %가 높은 영화 순으로 나열 (좋아요 수 10개 이상부터 보여주기.)
	@Query(value = "select movieseqno, "
			+ "count(*) as totalCount, "
			+ "count(CASE WHEN likecheck = 'Y' THEN 1 END) as likeCount, "
			+ "round(count(case when likecheck = 'Y' THEN 1 END) * 100.0 / count(*),2) as likePercentage "
			+ "from tbl_like "
			+ "group by movieseqno "
			+ "having count(case when likecheck = 'Y' then 1 end) >= 10 "
			+ "order by likePercentage DESC", nativeQuery = true)
	public List<Object[]> likeResult();
	
	
	//각 영화에 대해 싫어요 %가 높은 영화 순으로 나열 (싫어요 수 10개 이상부터 보여주기.)
	@Query(value = "select movieseqno, "
			+ "count(*) as totalCount, "
			+ "count(CASE WHEN dislikecheck = 'Y' THEN 1 END) as dislikeCount, "
			+ "round(count(case when dislikecheck = 'Y' THEN 1 END) * 100.0 / count(*),2) as dislikePercentage "
			+ "from tbl_like "
			+ "group by movieseqno "
			+ "having count(case when dislikecheck = 'Y' then 1 end) >= 10 "
			+ "order by dislikePercentage DESC", nativeQuery = true)
	public List<Object[]> dislikeResult();
	
	
	//호불호가 높은 영화 순으로 나열 (totalCount 수 20개 이상부터 보여주기.)
	@Query(value = "select movieseqno, "
			+ "count(*) as totalCount, "
			+ "count(CASE WHEN likecheck = 'Y' THEN 1 END) as likeCount, "
			+ "count(CASE WHEN dislikecheck = 'Y' THEN 1 END) as dislikeCount, "
			+ "round(count(case when likecheck = 'Y' THEN 1 END) * 100.0 / count(*),0) as likePercentage, "
			+ "round(count(case when dislikecheck = 'Y' THEN 1 END) * 100.0 / count(*),0) as dislikePercentage "
			+ "from tbl_like "
			+ "group by movieseqno "
			+ "having (count(*) >= 20) and (count(case when likecheck = 'Y' THEN 1 END) * 100.0 / count(*) <= 60) and (count(case when dislikecheck = 'Y' THEN 1 END) * 100.0 / count(*) <=60) "
			+ "order by totalCount DESC", nativeQuery = true)
	public List<Object[]> divisiveResult();

	
}

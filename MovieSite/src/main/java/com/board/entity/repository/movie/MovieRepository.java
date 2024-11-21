package com.board.entity.repository.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.hibernate.annotations.QueryCacheLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.board.entity.MemberEntity;
import com.board.entity.movie.MovieEntity;
import java.util.List;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{
	
	//게시물 목록 보기...배우이름?
	public Page<MovieEntity> findByTitleContainingOrOriginalTitleContaining(String keyword, String keyword2, Pageable pageable);
	public List<MovieEntity> findByTitleContaining(String keyword);

	public List<MovieEntity> findByTitleContainingIgnoreCase(String keyword);
	public MovieEntity findByTitle(String title);
//	public Optional<MovieEntity> findByMovieNm(String movieNm);
//	public MovieEntity findByMovieCd(String movieCd);
	
	//관리자페이지 : 영화 수정 - Certification 이 null 인 영화 리스트 뽑아내기 (북마크 높은 순으로)
	@Query("SELECT m, COUNT(b) "
		     + "FROM MovieEntity m "
		     + "LEFT JOIN bookmark b ON m.movieId = b.movieId.movieId "
		     + "WHERE m.certification IS NULL "
		     + "GROUP BY m "
		     + "ORDER BY COUNT(b) DESC")
	public List<Object[]> findByCert();


}

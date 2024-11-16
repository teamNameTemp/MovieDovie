package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.LikeRepository;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.movie.MovieRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Service
@AllArgsConstructor
@Log4j2
class LikeServiceImpl implements LikeService{
	
	public final LikeRepository likeRepository;
	public final MemberRepository memberRepository;
	public final MovieRepository movieRepository;
	
	//좋아요 % 높은 영화 추출
	@Override
	public List<Object[]> topLikeMovie(){
		return likeRepository.likeResult();
	}
	
	//싫어요 % 높은 영화 추출
	@Override
	public List<Object[]> topDislikeMovie(){
		return likeRepository.dislikeResult();
	}
	
	//호불호 % 높은 영화 추출
	@Override	
	public List<Object[]> topDivisiveMovie(){
		return likeRepository.divisiveResult();
	}

	@Override
	public void saveOrUpdate(String rating, Long movieId, String email) {
		MemberEntity member = memberRepository.findById(email).get();
		MovieEntity movie = movieRepository.findById(movieId).
				 orElseThrow(() -> new RuntimeException("Movie not found"));
	
		String likeCheck = "N";
		String dislikeCheck = "N";
		
		switch(rating) {
		case "like":
			likeCheck="Y";
			dislikeCheck="N";
			break;
		case "dislike":
			likeCheck="N";
			dislikeCheck="Y";
			break;
			default:
				break;
		}
		LikeEntity likeEntity = likeRepository.findByEmailAndMovieId(member,movie);
		//저장
		if(likeEntity == null) {
			likeEntity = LikeEntity.builder()
								.likecheck(likeCheck)
								.dislikecheck(dislikeCheck)
								.email(member)
								.movieId(movie)
								.build();
		}
		//업데이트
		else {
			likeEntity.setLikecheck(likeCheck);
			likeEntity.setDislikecheck(dislikeCheck);
		}
		
		likeRepository.save(likeEntity);
	}

	@Override
	public Map<String,Object> getLikeInfo(String email, Long movieId) {
		Map<String,Object> data = new HashMap<>();
		
		MemberEntity member = memberRepository.findByEmail(email).get();
		 MovieEntity movie = movieRepository.findById(movieId).
				 orElseThrow(() -> new RuntimeException("Movie not found"));
		LikeEntity like = likeRepository.findByEmailAndMovieId(member, movie);
		if(like == null) {
			return data;
		}else {
			data.put("like",like.getLikecheck());
			data.put("dislike",like.getDislikecheck());
			return data;
		}
	}

	@Override
	public Map<String, Object> getAllLikeInfo(Long movieId) {
	    Map<String, Object> data = new HashMap<>();
	    MovieEntity movie = movieRepository.findById(movieId)
	            .orElseThrow(() -> new RuntimeException("Movie not found"));
	    List<LikeEntity> allLike = likeRepository.findAllByMovieId(movie);
	    
	    // 좋아요 개수 계산
	    long likeCount = allLike.stream()
	        .filter(like -> "Y".equals(like.getLikecheck()))
	        .count();

	    // 싫어요 개수 계산
	    long dislikeCount = allLike.stream()
	        .filter(like -> "Y".equals(like.getDislikecheck()))
	        .count();

	    // 결과를 Map에 저장
	    data.put("likeCount", likeCount);
	    data.put("dislikeCount", dislikeCount);
	    data.put("totalCount", allLike.size());

	    return data;
	}
}

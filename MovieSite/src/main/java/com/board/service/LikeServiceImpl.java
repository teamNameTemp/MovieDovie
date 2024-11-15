package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.LikeDTO;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.LikeRepository;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.movie.MovieRepository;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService{
	
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
		MovieEntity movie = movieRepository.findById(movieId).get();
		
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
	public LikeDTO getLikeInfo(String email, Long movieId) {
		MemberEntity member = memberRepository.findByEmail(email).get();
		MovieEntity movie = movieRepository.findById(movieId).get();
		LikeEntity like = likeRepository.findByEmailAndMovieId(member, movie);
		if(like == null) {
			return null;
		}
		else {
			return new LikeDTO(like);
		}
	}
}

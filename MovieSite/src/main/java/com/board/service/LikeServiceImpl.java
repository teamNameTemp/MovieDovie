package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.entity.repository.LikeRepository;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService{
	
	public final LikeRepository likeRepository;
	
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

}

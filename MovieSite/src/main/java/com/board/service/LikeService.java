package com.board.service;

import java.util.List;

import com.board.dto.LikeDTO;

public interface LikeService {

	//좋아요 % 높은 영화 추출
	public List<Object[]> topLikeMovie();
	
	//싫어요 % 높은 영화 추출
	public List<Object[]> topDislikeMovie();
	
	//호불호 % 높은 영화 추출
	public List<Object[]> topDivisiveMovie();

	public void saveOrUpdate(String rating, Long movieId, String email);
	public LikeDTO getLikeInfo(String email, Long movieId);
}

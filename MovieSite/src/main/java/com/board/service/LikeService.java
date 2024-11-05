package com.board.service;

import java.util.List;

public interface LikeService {

	//좋아요 % 높은 영화 추출
	public List<Object[]> topLikeMovie();
	
	//싫어요 % 높은 영화 추출
	public List<Object[]> topDislikeMovie();
	
	//호불호 % 높은 영화 추출
	public List<Object[]> topDivisiveMovie();
	
}

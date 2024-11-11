package com.board.service;

import java.util.List;

import com.board.dto.FavGenreDTO;
import com.board.entity.MemberEntity;

public interface FavGenreService {

	//회원들이 좋아하는 장르 인기 순으로 뽑아내기
	public List<Object[]> favGenreResult();
	
	public void saveTheFavGenre(FavGenreDTO favGenre);
}

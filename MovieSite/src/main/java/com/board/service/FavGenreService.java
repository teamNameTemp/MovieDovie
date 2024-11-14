package com.board.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.board.dto.FavGenreDTO;

public interface FavGenreService {

	//회원들이 좋아하는 장르 인기 순으로 뽑아내기
	public List<Object[]> favGenreResult();
	
	public void saveTheFavGenre(FavGenreDTO favGenre);
	
	public Optional<Map<String,Object>> getFavGenre();
}

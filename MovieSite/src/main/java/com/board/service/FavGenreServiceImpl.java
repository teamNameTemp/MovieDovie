package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.entity.repository.FavGenreRepository;
import com.board.entity.repository.LikeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FavGenreServiceImpl implements FavGenreService {

	public final FavGenreRepository favGenreRepository;
	
	//회원들이 좋아하는 장르 인기 순으로 뽑아내기
	@Override	
	public List<Object[]> favGenreResult(){
		return favGenreRepository.favGenreResult();
	}
}

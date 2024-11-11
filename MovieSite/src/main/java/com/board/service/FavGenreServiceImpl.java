package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.FavGenreDTO;
import com.board.entity.FavGenreEntity;
import com.board.entity.MemberEntity;
import com.board.entity.repository.FavGenreRepository;

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

	@Override
	public void saveTheFavGenre(FavGenreDTO favGenre) {
		FavGenreEntity favGenreEntity = favGenre.dtoToEntity();
		
		favGenreRepository.save(favGenreEntity);
	}

}

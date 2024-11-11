package com.board.service;

import java.util.Collections;
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
		
		List<Object[]> result = favGenreRepository.favGenreResult();
		    
	    if (result == null || result.isEmpty()) {
	        // 데이터가 없으면 빈 리스트를 반환하거나 적절한 처리를 할 수 있습니다.
	        return Collections.emptyList();  // 또는 다른 처리가 필요하면 여기에서 수행
	    }
	    
	    return result;
	    
	}
}

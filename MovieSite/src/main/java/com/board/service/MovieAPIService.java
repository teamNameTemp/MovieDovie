package com.board.service;

import java.util.List;
import java.util.Map;


import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.GenreEntity;
import com.board.entity.movie.MovieEntity;

public interface MovieAPIService {
	//DB에 저장되어 있는 영화상세정보가져오기
	public Map<String,Object> getMovieDetails(Long id);
	
	//해당 장르리스트 가져오기
	public List<GenreEntity> getGenreList(String genre);
	
	//같은 장르의 영화리스트 가져오기
	public List<Map<String,Object>> findByGenreGetMovieList(List<GenreEntity> genres);
	
	//키워드로 DB에 저장되어 있는 영화가져오기
	public List<Map<String,Object>> findByKeywordMovieList(String keyword);
	
	//저장 및 업데이트
	public void saveTheDataBases(List<MovieDTO> list);
	public void saveTheDataBase(MovieDTO movie);
	
	//관리자페이지 : 영화 수정 - Certification 이 null 인 영화 리스트 뽑아내기
	public List<Object[]> findByCert();
	
	
}

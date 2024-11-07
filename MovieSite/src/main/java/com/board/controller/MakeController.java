package com.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.MovieEntity;
import com.board.service.MovieAPIServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MakeController {
	private final MovieAPIServiceImpl movie;
	
	@GetMapping("/make/movie")
	public void getMake() {
		
	}
	
	@PostMapping("/make/movies/save")
	public void postMovies() {
		List<MovieDTO> list = movie.getTrendingMovies("day", 1, "ko-KR").block();
		List<MovieEntity> listEntity = new ArrayList<>();
		MovieEntity e = new MovieEntity();
		for(MovieDTO dto : list) {
			MovieEntity entity = dto.convertToEntity();
			listEntity.add(entity);
			e = dto.convertToEntity();
		}
		
		movie.saveTheDataBase(e);
		movie.saveTheDataBases(listEntity);
		
	}
}

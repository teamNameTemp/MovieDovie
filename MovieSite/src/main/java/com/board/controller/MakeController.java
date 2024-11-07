package com.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.MovieEntity;
import com.board.service.MovieAPIServiceImpl;
import com.board.service.TmdbAPIServiceImpl;
import com.board.util.MovieMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MakeController {
	private final MovieAPIServiceImpl movie;
	private final TmdbAPIServiceImpl tmdb;
	private final MovieMapper mapper;
	
	@GetMapping("/make/movie")
	public void getMake() {
		
	}
	
	@PostMapping("/make/movies/save")
	public void postMovies() {
		List<MovieDTO> list = movie.getTrendingMovies("day", 1, "ko-KR").block();
		List<MovieDTO> temp = new ArrayList<>();
		
		for(MovieDTO d : list) {
			MovieDTO dto = movie.getMovieDetails(d.getId(), "ko-KR").block();
			temp.add(dto);
		}
		List<MovieEntity> infoList = mapper.toEntityList(temp);
		
		movie.saveTheDataBases(infoList);
		
	}
}

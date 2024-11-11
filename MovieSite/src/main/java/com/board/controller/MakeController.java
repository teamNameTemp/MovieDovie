package com.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.movie.MovieDTO;
import com.board.service.MovieAPIServiceImpl;
import com.board.service.TmdbAPIServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MakeController {
    private final MovieAPIServiceImpl movie;
    private final TmdbAPIServiceImpl tmdb;
    
    @GetMapping("/make/movie")
    public void getMake() {
        
    }
    
    @PostMapping("/make/movies/save-all")
    public void postSaveAll(){
    	  List<MovieDTO> list = movie.getTrendingMovies("day", 1, "ko-KR").block();
          List<MovieDTO> temp = new ArrayList<>();
          
          for(MovieDTO d : list) {
              MovieDTO dto = movie.getMovieDetails(d.getId(), "ko-KR").block();
              temp.add(dto);
          }

          movie.saveTheDataBases(temp);
    	
    }
    
    @PostMapping("/make/movies/save")
    @ResponseBody
    public void postMovies(@RequestParam("movieId") Long movieId) {
    	
    	MovieDTO dto = movie.getMovieDetails(movieId, "ko-KR").block();

        movie.saveTheDataBase(dto);
    }
	
	
    @PostMapping("/make/movie/searchKeyword")
	@ResponseBody
	public List<MovieDTO> postSearchKeyword(@RequestParam("keyword") String keyword){
    	List<MovieDTO> list = tmdb.searchMovies(keyword).block();
    	log.info(list);
    	
//    	log.info(tmdb.searchPerson(keyword).block());
    	return list;
    }


    @PostMapping("/make/movie/searchTrend")
    @ResponseBody
    public List<MovieDTO> postSearchTrend() {
    	List<MovieDTO> list = tmdb.getTrendingMovies("day", 1, "ko-KR").block();
    	return list;
    }
}
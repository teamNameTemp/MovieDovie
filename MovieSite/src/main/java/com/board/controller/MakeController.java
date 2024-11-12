package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public void getMake(Model model) {
    	model.addAttribute("genres",tmdb.getGenreList().block());
    }
    
    @PostMapping("/make/movie/save-all")
    public void postSaveAll(){
    	  List<MovieDTO> list = movie.getTrendingMovies("day", 1, "ko-KR").block();
          List<MovieDTO> temp = new ArrayList<>();
          
          for(MovieDTO d : list) {
              MovieDTO dto = movie.getMovieDetails(d.getId(), "ko-KR").block();
              temp.add(dto);
          }

          movie.saveTheDataBases(temp);
    	
    }
    
    @PostMapping("/make/movie/save")
    @ResponseBody
    public Map<String,String> postMovies(@RequestParam("movieId") Long movieId) {
    	MovieDTO dto = movie.getMovieDetails(movieId, "ko-KR").block();
    	Map<String,String> data = new HashMap<>();
    	
    	//todo : 저장이 안될 시 오류처리 코드 작성해야함
    	data.put("message", "good");
    	
    	movie.saveTheDataBase(dto);
    	return data;
    }
	
	
    @PostMapping("/make/movie/searchKeyword")
	@ResponseBody
	public List<MovieDTO> postSearchKeyword(@RequestParam("keyword") String keyword){
    	List<MovieDTO> list = tmdb.searchMovies(keyword).block();
    	
//    	log.info(tmdb.searchPerson(keyword).block());
    	return list;
    }


    @PostMapping("/make/movie/searchTrend")
    @ResponseBody
    public List<MovieDTO> postSearchTrend() {
    	List<MovieDTO> list = tmdb.getTrendingMovies("day", 1, "ko-KR").block();
    	return list;
    }
    
    @PostMapping("/make/movie/genre")
    @ResponseBody
    public Map<String,String> postSaveGenre(@RequestParam("genre") String genreNm){
        log.info(genreNm);
        Map<String,String> map = new HashMap<>();
        
        List<MovieDTO> list = tmdb.getMoviesByGenreName(genreNm, 1).block();
        movie.saveTheDataBases(list);
        
        map.put("message", "good");
        return map;
    }
}
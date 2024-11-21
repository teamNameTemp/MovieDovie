package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.convert.DtoInstantiatingConverter;
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
    @ResponseBody
    public Map<String,Object> postSaveAll(){
    	  List<MovieDTO> list = tmdb.getTrendingMovies("day", 1, "ko-KR").block();
          List<MovieDTO> temp = new ArrayList<>();
          
          for(MovieDTO d : list) {
              MovieDTO dto = tmdb.getMovieDetails(d.getId(), "ko-KR").block();
              temp.add(dto);
              log.info(dto.getVideos().getResults().size() + " : " + dto.getTitle());
          }

          movie.saveTheDataBases(temp);
    	Map<String,Object> data = new HashMap<>();
    	
    	data.put("message", "good");
    	
    	return data;
    }
    
    @PostMapping("/make/movie/save")
    @ResponseBody
    public Map<String,String> postMovies(@RequestParam("movieId") Long movieId) {
    	MovieDTO dto = tmdb.getMovieDetails(movieId, "ko-KR").block();
    	Map<String,String> data = new HashMap<>();
    	
    	//todo : 저장이 안될 시 오류처리 코드 작성해야함
    	data.put("message", "good");
    	
    	movie.saveTheDataBase(dto);
    	return data;
    }
	
    @PostMapping("/make/movie/searchKeyword")
	@ResponseBody
	public List<Map<String,Object>> postSearchKeyword(@RequestParam("keyword") String keyword){
    	// List<List<MovieDTO>> list = tmdb.searchMovies(keyword,2).block();

        List<Map<String,Object>> list = movie.findByKeywordMovieList(keyword);
//    	log.info(tmdb.searchPerson(keyword).block());
    	return list;
    }


    @PostMapping("/make/movie/searchTrend")
    @ResponseBody
    public List<MovieDTO> postSearchTrend() {
    	List<MovieDTO> list = tmdb.getTrendingMovies("day", 1, "ko-KR").block();
    	return list;
    }
    
    @PostMapping("/api/movies/save-by-genre")
    @ResponseBody
    public String saveMoviesByGenre(@RequestParam("genreId") Long genreId) {
        try {
            List<MovieDTO> movies = tmdb.getMoviesByGenre(genreId, 1).block();
            List<MovieDTO> list = new ArrayList<>();
            for(MovieDTO dto : movies) {
            	MovieDTO d = tmdb.getMovieDetails(dto.getId(), "ko-KR").block();
            	list.add(d);
            }
            movie.saveTheDataBases(list);
            return  genreId + ":" + list.size() + "개가 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            return "영화 저장 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
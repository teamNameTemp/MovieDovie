package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@Service
public class MovieAPIServiceImpl implements MovieAPIService {
	private final TmdbAPIServiceImpl tmdb;
	private final MovieRepository movieRepository;
	
	public Mono<List<MovieDTO>> getTrendingMovies(String timeWindow, int page, String language){
		return tmdb.getTrendingMovies(timeWindow, page, language);
	}
	
	public Mono<MovieDTO> getDiscoverMovies(String sortBy, String withGenres, int page, String language){
		return tmdb.getDiscoverMovies(sortBy, withGenres, page, language);
	}
	
	public Mono<MovieDTO> getMovieDetails(Long movieId,String language){
		return tmdb.getMovieDetails(movieId,language);
	}
	
	public void saveTheDataBases(List<MovieEntity> list) {
		movieRepository.saveAll(list);
	}
	
	public void saveTheDataBase(MovieEntity movie) {
		movieRepository.save(movie);
	}


}

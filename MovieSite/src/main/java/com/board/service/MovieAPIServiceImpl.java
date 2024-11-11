package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.CastEntity;
import com.board.entity.movie.CompanyEntity;
import com.board.entity.movie.CountryEntity;
import com.board.entity.movie.CreditEntity;
import com.board.entity.movie.CrewEntity;
import com.board.entity.movie.GenreEntity;
import com.board.entity.movie.LanguageEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.movie.CastRepository;
import com.board.entity.repository.movie.CompanyRepository;
import com.board.entity.repository.movie.CountryRepository;
import com.board.entity.repository.movie.CreditRepository;
import com.board.entity.repository.movie.CrewRepository;
import com.board.entity.repository.movie.GenreRepository;
import com.board.entity.repository.movie.LanguageRepository;
import com.board.entity.repository.movie.MovieRepository;
import com.board.util.MovieMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@Service
public class MovieAPIServiceImpl implements MovieAPIService {
	private final TmdbAPIServiceImpl tmdb;
	private final MovieRepository movieRepository;
	private final CompanyRepository companyRepository;
	private final CountryRepository countryRepository;
	private final GenreRepository genreRepository;
	private final CreditRepository creditRepository;
	private final CrewRepository crewRepository;
	private final CastRepository castRepository;
	private final LanguageRepository languageRepository;
	private final MovieMapper mapper;
	
	public Mono<List<MovieDTO>> getTrendingMovies(String timeWindow, int page, String language){
		return tmdb.getTrendingMovies(timeWindow, page, language);
	}
	
	public Mono<MovieDTO> getDiscoverMovies(String sortBy, String withGenres, int page, String language){
		return tmdb.getDiscoverMovies(sortBy, withGenres, page, language);
	}
	
	public Mono<MovieDTO> getMovieDetails(Long movieId,String language){
		return tmdb.getMovieDetails(movieId,language);
	}
	
	public void saveTheDataBases(List<MovieDTO> list) {
		List<MovieEntity> movieEntities  = mapper.toEntityList(list);
		movieRepository.saveAll(movieEntities);
		for(MovieDTO dto : list) {
				List<CompanyEntity> companies = mapper.mapProductionCompanies(dto);
				List<CountryEntity> countries = mapper.mapProductionCountries(dto);
				List<GenreEntity> genres = mapper.mapGenres(dto);
				CreditEntity credit = mapper.mapCredits(dto);
				List<CrewEntity> crews = mapper.mapCrews(dto.getCredits());
				List<CastEntity> casts = mapper.mapCasts(dto.getCredits());
				List<LanguageEntity>languages = mapper.mapSpokenLanguages(dto);
				
				companyRepository.saveAll(companies);
				countryRepository.saveAll(countries);
				genreRepository.saveAll(genres);
				creditRepository.save(credit);
				crewRepository.saveAll(crews);
				castRepository.saveAll(casts);
				languageRepository.saveAll(languages);
			}
	}
		
	
	public void saveTheDataBase(MovieEntity movie) {
		movieRepository.save(movie);
	}


}

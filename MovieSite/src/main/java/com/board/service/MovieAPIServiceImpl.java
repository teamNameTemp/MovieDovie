package com.board.service;

import java.util.List;
import java.util.Optional;

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
import com.board.entity.movie.VideoEntity;
import com.board.entity.repository.movie.CastRepository;
import com.board.entity.repository.movie.CompanyRepository;
import com.board.entity.repository.movie.CountryRepository;
import com.board.entity.repository.movie.CreditRepository;
import com.board.entity.repository.movie.CrewRepository;
import com.board.entity.repository.movie.GenreRepository;
import com.board.entity.repository.movie.LanguageRepository;
import com.board.entity.repository.movie.MovieRepository;
import com.board.entity.repository.movie.VideoRepository;
import com.board.util.MovieMapper;

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
	private final VideoRepository videoRepository;
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
		for(MovieDTO dto : list) {
			MovieEntity entity = movieRepository.findById(dto.getId()).get();
			if(entity == null) {
				saveTheDataBase(dto);
			}
			else {
				mapperListSave(dto);
			}
		}
	}
		
	
	public void saveTheDataBase(MovieDTO movie) {
		MovieEntity movieEntity = mapper.toEntity(movie);
		movieRepository.save(movieEntity);
		
		//todo : 리스트(배우,감독,크레딧,언어,비디오, 장르, 제작사, 나라)
		mapperListSave(movie);
		
	}

	private void mapperListSave(MovieDTO dto) {
		List<CompanyEntity> companies = mapper.mapProductionCompanies(dto);
		List<CountryEntity> countries = mapper.mapProductionCountries(dto);
		List<GenreEntity> genres = mapper.mapGenres(dto);
		CreditEntity credit = mapper.mapCredits(dto);
		List<CrewEntity> crews = mapper.mapCrews(dto.getCredits());
		List<CastEntity> casts = mapper.mapCasts(dto.getCredits());
		List<LanguageEntity>languages = mapper.mapSpokenLanguages(dto);
		List<VideoEntity> videos = mapper.mapVideos(dto);
		
		companyRepository.saveAll(companies);
		countryRepository.saveAll(countries);
		genreRepository.saveAll(genres);
		creditRepository.save(credit);
		crewRepository.saveAll(crews);
		castRepository.saveAll(casts);
		languageRepository.saveAll(languages);
		videoRepository.saveAll(videos);
	}

}

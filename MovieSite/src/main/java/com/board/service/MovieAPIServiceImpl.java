package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.CastEntity;
import com.board.entity.movie.CompanyEntity;
import com.board.entity.movie.CountryEntity;
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

	public Map<String, Object> getMovieDetails(Long id) {
		Map<String, Object> data = new HashMap<>();
		MovieEntity entity = movieRepository.findById(id).get();

		if (entity == null) {
			data.put("movie", tmdb.getMovieDetails(id, "ko-KR").block());
			return data;
		} else {

			data.put("movie", entity);
			
			//아래 엔티티들 리스트로 뽑을 수 있게 리포지토리 쿼리문 변경
//			data.put("company", mapper.toMapEntity(companyRepository.findById(id).get()));
//			data.put("country", mapper.toMapEntity(countryRepository.findById(id).get()));
//			data.put("cast", mapper.toMapEntity(castRepository.findById(id).get()));
//			data.put("crew", mapper.toMapEntity(crewRepository.findById(id).get()));
//			data.put("genre", mapper.toMapEntity(genreRepository.findById(id).get()));
//			data.put("video", mapper.toMapEntity(videoRepository.findById(id).get()));
//			data.put("language", mapper.toMapEntity(languageRepository.findById(id).get()));
			
			return data;
		}
	}

	public Mono<List<MovieDTO>> getTrendingMovies(String timeWindow, int page, String language) {
		return tmdb.getTrendingMovies(timeWindow, page, language);
	}

	public Mono<MovieDTO> getDiscoverMovies(String sortBy, String withGenres, int page, String language) {
		return tmdb.getDiscoverMovies(sortBy, withGenres, page, language);
	}

	public Mono<MovieDTO> getMovieDetails(Long movieId, String language) {
		return tmdb.getMovieDetails(movieId, language);
	}

	//todo : 저장 및 업데이트
	public void saveTheDataBases(List<MovieDTO> list) {
	    for (MovieDTO dto : list) {
	    	//DB에 없으면(저장)
	    	if(movieRepository.findById(dto.getId()).isEmpty()) {
	    		saveTheDataBase(dto);
	    	}
	    	else { // 있으면(업데이트)
	    		MovieEntity entity = movieRepository.findById(dto.getId()).get();
	    		movieRepository.save(entity);
	    		
	    		mapperListSave(dto);
	    	}
	    }
	}

	
	public void saveTheDataBase(MovieDTO movie) {
		MovieEntity movieEntity = mapper.toEntity(movie);
		movieRepository.save(movieEntity);

		// todo : 리스트(배우,감독,크레딧,언어,비디오, 장르, 제작사, 나라)
		mapperListSave(movie);

	}

	private void mapperListSave(MovieDTO dto) {
		//중복 체크 (있으면 업데이트)
		List<CompanyEntity> companies = companyRepository.findByMovieId(dto.getId());
//		List<CountryEntity> countries = countryRepository.findById(dto.getId())
//		List<GenreEntity> genres = genreRepository.findById(dto.getId())
//		List<CastEntity> casts = castRepository.findById(dto.getId())
//		List<CrewEntity> crews = crewRepository.findById(dto.getId())
//		List<LanguageEntity> languages = languageRepository.findById(dto.getId())
//		List<VideoEntity> videos = videoRepository.findById(dto.getId())

		
		//없으면 저장
//		List<CompanyEntity> companies = mapper.mapProductionCompanies(dto);
		List<CountryEntity> countries = mapper.mapProductionCountries(dto);
		List<GenreEntity> genres = mapper.mapGenres(dto);

		if(dto.getCredits() != null) {
			
			List<CrewEntity> crews = mapper.mapCrews(dto);
			List<CastEntity> casts = mapper.mapCasts(dto);
			
			crewRepository.saveAll(crews);
			castRepository.saveAll(casts);
		}
		
		List<LanguageEntity> languages = mapper.mapSpokenLanguages(dto);
		List<VideoEntity> videos = mapper.mapVideos(dto);

//		companyRepository.saveAll(companies);
		countryRepository.saveAll(countries);
		genreRepository.saveAll(genres);
		languageRepository.saveAll(languages);
		videoRepository.saveAll(videos);
	}

}

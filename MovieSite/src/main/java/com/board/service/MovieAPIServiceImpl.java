package com.board.service;

import java.util.ArrayList;
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
import com.board.entity.movie.ImageEntity;
import com.board.entity.movie.LanguageEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.movie.VideoEntity;
import com.board.entity.repository.movie.CastRepository;
import com.board.entity.repository.movie.CompanyRepository;
import com.board.entity.repository.movie.CountryRepository;
import com.board.entity.repository.movie.CrewRepository;
import com.board.entity.repository.movie.GenreRepository;
import com.board.entity.repository.movie.ImageRepository;
import com.board.entity.repository.movie.LanguageRepository;
import com.board.entity.repository.movie.MovieRepository;
import com.board.entity.repository.movie.VideoRepository;
import com.board.util.MovieMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class MovieAPIServiceImpl implements MovieAPIService {
	private final TmdbAPIServiceImpl tmdb;
	private final MovieRepository movieRepository;
	private final CompanyRepository companyRepository;
	private final CountryRepository countryRepository;
	private final GenreRepository genreRepository;
	private final CrewRepository crewRepository;
	private final CastRepository castRepository;
	private final LanguageRepository languageRepository;
	private final VideoRepository videoRepository;
	private final ImageRepository imageRepository;
	private final MovieMapper mapper;

	//DB에 저장되있는거 가져오기
	@Override
	public Map<String, Object> getMovieDetails(Long id) {
		Map<String, Object> data = new HashMap<>();
		MovieEntity entity = movieRepository.findById(id).get();

		if (entity == null) {
			data.put("movie", tmdb.getMovieDetails(id, "ko-KR").block());
			return data;
		} else {
			data.put("info", entity);
			
			//아래 엔티티들 리스트로 뽑을 수 있게 리포지토리 쿼리문 변경
			data.put("company", mapper.getCompanyList(companyRepository.findByMovieId(entity)));
			data.put("country", mapper.getCountryList(countryRepository.findByMovieId(entity)));
			data.put("cast", mapper.getCastList(castRepository.findByMovieId(entity)));
			data.put("crew", mapper.getCrewList(crewRepository.findByMovieId(entity)));
			data.put("genre", mapper.getGenreList(genreRepository.findByMovieId(entity)));
			data.put("video", mapper.getVideoList(videoRepository.findByMovieId(entity)));
			data.put("language", mapper.getLanguageList(languageRepository.findByMovieId(entity)));
			data.put("backdrops", mapper.getBackdropList(imageRepository.findByMovieId(entity)));
			data.put("posters", mapper.getPosterList(imageRepository.findByMovieId(entity)));
			
			return data;
		}
	}
	@Override
	public List<GenreEntity> getGenreList(String genre){
		List<GenreEntity> genres = genreRepository.findByGenreNm(genre).stream().limit(20).toList();
		if(genres == null) return new ArrayList<>();
		
		return genres;
	}
	
	//같은 장르의 영화리스트 가져오기
	@Override
	public List<Map<String,Object>> findByGenreGetMovieList(List<GenreEntity> genres){
		List<Map<String,Object>> data = new ArrayList<>();
		
		for(GenreEntity genre : genres) {
			data.add(mapper.getMovieSimpleInfo(genre.getMovieId()));
		}
		
		return data;
	}
	//todo : 저장 및 업데이트
	@Override
	public void saveTheDataBases(List<MovieDTO> list) {
	    for (MovieDTO dto : list) {
	    	if(movieRepository.findById(dto.getId()).isEmpty()) {
	    		saveTheDataBase(dto);
	    	}
	    	else { // 있으면(업데이트)
	    		MovieEntity entity = movieRepository.findById(dto.getId()).get();	    		
	    		entity.setOverview(dto.getOverview());
	    		entity.setIsAdult(dto.isAdult());
	    		movieRepository.save(entity);
	    		dto.setEntity(entity);
	    	}
	    	
	    	mapperListSave(dto);
	    }
	}

	@Override
	public void saveTheDataBase(MovieDTO movie) {
		MovieEntity movieEntity = mapper.toEntity(movie);
		movieRepository.save(movieEntity);
	}

	//저장 및 업데이트
	private void mapperListSave(MovieDTO dto) {

		//있는지 확인
		MovieEntity movie = movieRepository.findById(dto.getId()).get();
		if(movie == null) return;
		
		//제작사 유무
		List<CompanyEntity> companies = companyRepository.findByMovieId(movie);
		if(companies.isEmpty()) {
			companies = mapper.mapProductionCompanies(dto);
		}
		companyRepository.saveAll(companies);
		
		//나라 유무
		List<CountryEntity> countries = countryRepository.findByMovieId(movie);
		if(countries.isEmpty()) {
			countries = mapper.mapProductionCountries(dto);
		}
		countryRepository.saveAll(countries);
		
		//장르 유무
		List<GenreEntity> genres = genreRepository.findByMovieId(movie);
		if(genres.isEmpty()) {
			genres = mapper.mapGenres(dto);
		}
		genreRepository.saveAll(genres);
		
		//크레딧 유무
		if(dto.getCredits() != null) {
			//제작진 유무
			List<CrewEntity> crews = crewRepository.findByMovieId(movie);
			if(crews.isEmpty()) {
				crews = mapper.mapCrews(dto);
			}
			crewRepository.saveAll(crews);
			//출연진 유무
			List<CastEntity> casts = castRepository.findByMovieId(movie);
			if(casts.isEmpty()) {
				casts = mapper.mapCasts(dto);
			}
			castRepository.saveAll(casts);
		}
		
		//언어 유무
		List<LanguageEntity> languages = languageRepository.findByMovieId(movie);
		if(languages.isEmpty()) {
			languages = mapper.mapSpokenLanguages(dto);
		}
		languageRepository.saveAll(languages);
		
		//비디오 유무
		List<VideoEntity> videos = videoRepository.findByMovieId(movie);
		if(videos.isEmpty() || videos == null) {
			log.info(videos.size() + dto.getId() +":"+ dto.getVideos().getResults().size());
			videos = mapper.mapVideos(dto);
		log.info(videos);
		}
		videoRepository.saveAll(videos);

		//이미지 유무
		List<ImageEntity> images = imageRepository.findByMovieId(movie);
		if(images.isEmpty()) {
			images.addAll(mapper.mapBackdropImage(dto));
			images.addAll(mapper.mapPosterImage(dto));
		}
		imageRepository.saveAll(images);
	}

	@Override
	public List<Map<String, Object>> findByKeywordMovieList(String keyword) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		List<MovieEntity> entityList = movieRepository.findByTitleContaining(keyword);
		if(entityList == null) return data;
		
		for(MovieEntity entity : entityList) {
			Map<String,Object> movie = mapper.getMovieSimpleInfo(entity);
			data.add(movie);
		}
		
		return data;
	}
}
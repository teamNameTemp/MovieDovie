package com.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.board.entity.movie.WeeklyTrendEntity;
import com.board.entity.repository.movie.CastRepository;
import com.board.entity.repository.movie.CompanyRepository;
import com.board.entity.repository.movie.CountryRepository;
import com.board.entity.repository.movie.CrewRepository;
import com.board.entity.repository.movie.GenreRepository;
import com.board.entity.repository.movie.ImageRepository;
import com.board.entity.repository.movie.LanguageRepository;
import com.board.entity.repository.movie.MovieRepository;
import com.board.entity.repository.movie.VideoRepository;
import com.board.entity.repository.movie.WeeklyTrendRepository;
import com.board.util.DataCalculate;
import com.board.util.MovieMapper;

import jakarta.transaction.Transactional;
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
	private final WeeklyTrendRepository weeklyTrendRepository;
	
	//매주 월요일 0시 0분 0초에 트렌드 영화 업데이트 및 저장
	@Scheduled(cron = "0 0 0 * * MON")
	@Transactional
	public void autoTrendListSave() {
		
		List<MovieDTO> trendDataList = tmdb.getTrendingMovies("week", 1, "ko-KR").block();
		List<MovieDTO> trend = new ArrayList<>();
		for(MovieDTO dto : trendDataList) {
			MovieDTO movie = tmdb.getMovieDetails(dto.getId(), "ko-KR").block();
			trend.add(movie);
		}
		
		log.info(trend.size() + "개의 트렌드 영화가 저장 및 업데이트 되었습니다.");
		saveWeeklyTrend(trend);
	}

	@Transactional
	public void saveWeeklyTrend(List<MovieDTO> list) {
		LocalDateTime now = LocalDateTime.now();
		saveTheDataBases(list);
		
		for(MovieDTO dto : list) {
			MovieEntity movie = movieRepository.findById(dto.getId()).get();
			WeeklyTrendEntity weekly = 
			weeklyTrendRepository.findByYearAndMonthAndMonthOfWeekAndMovieId(
				now.getYear(), 
				now.getMonthValue(), 
				DataCalculate.getWeekOfMonth(now), 
				movie);
			
			if(weekly == null) {
				weekly = WeeklyTrendEntity.builder()
				.year(now.getYear())
				.month(now.getMonthValue())
				.monthOfWeek(DataCalculate.getWeekOfMonth(now))
				.movieId(movie)
				.build();
			
				weeklyTrendRepository.save(weekly);
			}
		}
	}

	public List<Map<String, Object>> getWeeklyTrend() {
		LocalDateTime now = LocalDateTime.now();
		List<WeeklyTrendEntity> list = 
		weeklyTrendRepository.findByYearAndMonthAndMonthOfWeek(now.getYear(), now.getMonthValue(), DataCalculate.getWeekOfMonth(now));
		
		List<Map<String, Object>> data = new ArrayList<>();
		for(WeeklyTrendEntity w : list) {
			Map<String, Object> map = mapper.getMovieSimpleInfo(w.getMovieId());
			data.add(map);
		}
		
		return data;

	}
	//매일 대여 가격 업데이트
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void autoUpdatePrice() {
		List<MovieEntity> list = movieRepository.findAll();
		int idx = 0;
		for(MovieEntity movie : list) {
			int price = DataCalculate.calcRentPrice(movie.getReleaseDate());
			if(price != movie.getPrice()) {
				movie.setPrice(price);
				idx++;
			}
		}
		
		log.info(idx + "개의 영화가 업데이트 되었습니다.");
		movieRepository.saveAll(list);
	}
	
	
	//DB에 저장되있는거 가져오기
	@Override
	public Map<String, Object> getMovieDetails(Long id) {
		Map<String, Object> data = new HashMap<>();
		Optional<MovieEntity> entity = movieRepository.findById(id);
		
		if (entity.isPresent()) {
			data.put("info", entity.get());
			
			//아래 엔티티들 리스트로 뽑을 수 있게 리포지토리 쿼리문 변경
			data.put("company", mapper.getCompanyList(companyRepository.findByMovieId(entity.get())));
			data.put("country", mapper.getCountryList(countryRepository.findByMovieId(entity.get())));
			data.put("cast", mapper.getCastList(castRepository.findByMovieId(entity.get())));
			data.put("crew", mapper.getCrewList(crewRepository.findByMovieId(entity.get())));
			data.put("genre", mapper.getGenreList(genreRepository.findByMovieId(entity.get())));
			data.put("video", mapper.getVideoList(videoRepository.findByMovieId(entity.get())));
			data.put("language", mapper.getLanguageList(languageRepository.findByMovieId(entity.get())));
			data.put("backdrops", mapper.getBackdropList(imageRepository.findByMovieId(entity.get())));
			data.put("posters", mapper.getPosterList(imageRepository.findByMovieId(entity.get())));
			
		}
		return data;
	}
	@Override
	public List<GenreEntity> getGenreList(String genre){
		List<GenreEntity> genres = genreRepository.findByGenreNm(genre).stream().limit(20).toList();
		if(genres == null) return new ArrayList<>();
		
		return genres;
	}
	
	//같은 장르의 영화리스트 가져오기
	@Override
	public List<Map<String,Object>> findByGenreGetMovieList(List<GenreEntity> genres,int age){
		List<Map<String,Object>> data = new ArrayList<>();
		
		for(GenreEntity genre : genres) {
			String certification = genre.getMovieId().getCertification();
			
			if(!DataCalculate.calcIsAdult(certification, age)) {
				continue;
			}
			Map<String,Object> map = mapper.getMovieSimpleInfo(genre.getMovieId());
			map.put("info", mapper.getMovieSimpleInfo(genre.getMovieId()));
			data.add(map);
		}
		
		return data;
	}
	
	@Override
	public List<Map<String,Object>> getDirectUploadList(int minusDay){
		List<Map<String,Object>> data = new ArrayList<>();
		
		//7일전까지의 업로드 리스트 가져오기
		LocalDateTime start = LocalDateTime.now().minusDays(minusDay);
		LocalDateTime end = LocalDateTime.now();
		
		List<MovieEntity> list = movieRepository.findAllByDirectUpload(start, end);
		
		for(MovieEntity m : list) {
			Map<String,Object> map = new HashMap<>();
			map.put("info", mapper.getMovieSimpleInfo(m));
			data.add(map);
		}
		
		
		return data;
	}
	
	@Override
	public void saveTheDataBases(List<MovieDTO> list) {
	    for (MovieDTO dto : list) {
	    	if(movieRepository.findById(dto.getId()).isEmpty()) {
	    		log.info(dto.getTitle() + "가 저장되었습니다.");
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
		movie.setEntity(movieEntity);
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
			videos = mapper.mapVideos(dto);
		}
		if(videos != null)videoRepository.saveAll(videos);

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
		
		List<MovieEntity> entityList = movieRepository.findByTitleContainingIgnoreCase(keyword);
		if(entityList == null) return data;
		
		for(MovieEntity entity : entityList) {
			Map<String,Object> movie = mapper.getMovieSimpleInfo(entity);
			movie.put("genre", genreRepository.findByMovieId(entity));
			data.add(movie);
		}
		
		return data;
	}
	
	//관리자페이지 : 영화 수정 - Certification 이 null 인 영화 리스트 뽑아내기
	@Override	
	public List<Object[]> findByCert(){
		
		return movieRepository.findByCert();
	}

	@Override
	@Modifying
	@Transactional
	public int clickToMovie(Long movieId) {
		MovieEntity movie = movieRepository.findById(movieId).get();
		int hitno = movie.getHitno()+1;
		
		movie.setHitno(hitno);
		movieRepository.save(movie);
		return hitno;
	}

	@Override
	public void directSave(MovieDTO movie) {
		MovieEntity movieEntity = mapper.toEntity(movie);
		movieEntity.setUploadCheck("Y");
		movieRepository.save(movieEntity);

		this.mapperListSave(movie);
	}

	@Override
	public List<Map<String, Object>> findByKeywordCrewList(String keyword) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		List<CrewEntity> entityList = crewRepository.findByCrewNmContainingIgnoreCase(keyword);
		if(entityList == null) return data;
		
		for(CrewEntity entity : entityList) {
			Map<String,Object> crew = new HashMap<>();
			crew.put("id", entity.getCrewId());
			crew.put("name",entity.getCrewNm());
			crew.put("profilePath", entity.getProfilePath());
			crew.put("movie",entity.getMovieId());
			data.add(crew);
		}
		
		return data;	
	}

	@Override
	public List<Map<String, Object>> findByKeywordProductionCompanyList(String keyword) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		List<CompanyEntity> entityList = companyRepository.findByCompanyNmContainingIgnoreCase(keyword);
		if(entityList == null) return data;
		
		for(CompanyEntity entity : entityList) {
			Map<String,Object> company = new HashMap<>();
			company.put("id", entity.getCompanyId());
			company.put("name", entity.getCompanyNm());
			company.put("logoPath", entity.getLogoPath());
			company.put("originCountry", entity.getOriginCountry());
			company.put("movie",entity.getMovieId());
			data.add(company);
		}
		
		return data;	
	}

	@Override
	public List<Map<String, Object>> findByKeywordCastList(String keyword) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		List<CastEntity> entityList = castRepository.findByCastNmContainingIgnoreCase(keyword);
		if(entityList == null) return data;
		
		for(CastEntity entity : entityList) {
			Map<String,Object> cast = new HashMap<>();
			cast.put("id", entity.getCastId());
			cast.put("name", entity.getCastNm());
			cast.put("character", entity.getCharacterNm());
			cast.put("profilePath", entity.getProfilePath());
			cast.put("movie",entity.getMovieId());
			data.add(cast);
		}
		
		return data;	
	}
	
}
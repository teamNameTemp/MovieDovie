package com.board.dto.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KobisMovieDTO {
	
	@JsonProperty("movieInfoResult")
	private MovieInfoResult movieInfoResult;

	@Data
	public static class MovieInfoResult {
		@JsonProperty("movieInfo")
		private MovieInfo movieInfo;
		@JsonProperty("source")
		private String source;
	}

	@Data
	@NoArgsConstructor
	public static class MovieInfo {
		@JsonProperty("movieCd")
		private String movieCd;
		@JsonProperty("movieNm")
		private String movieNm;
		@JsonProperty("movieNmEn")
		private String movieNmEn;
		@JsonProperty("movieNmOg")
		private String movieNmOg;
		@JsonProperty("showTm")
		private String showTm;
		@JsonProperty("prdtYear")
		private String prdtYear;
		@JsonProperty("openDt")
		private String openDt;
		@JsonProperty("prdtStatNm")
		private String prdtStatNm;
		@JsonProperty("typeNm")
		private String typeNm;
		@JsonProperty("overview")
		private String overview;
		@JsonProperty("posterPath")
		private String posterPath;

		@JsonProperty("nations")
		private List<Nation> nations;
		@JsonProperty("genres")
		private List<Genre> genres;
		@JsonProperty("directors")
		private List<Director> directors;
		@JsonProperty("actors")
		private List<Actor> actors;
		@JsonProperty("showTypes")
		private List<ShowType> showTypes;
		@JsonProperty("companys")
		private List<Company> companys;
		@JsonProperty("audits")
		private List<Audit> audits;
		@JsonProperty("staffs")
		private List<Staff> staffs;

		@Data
		public static class Nation {
			@JsonProperty("nationNm")
			private String nationNm;
		}

		@Data
		public static class Genre {
			@JsonProperty("genreNm")
			private String genreNm;
		}

		@Data
		public static class Director {
			@JsonProperty("peopleNm")
			private String peopleNm;
			@JsonProperty("peopleNmEn")
			private String peopleNmEn;
		}

		@Data
		public static class Actor {
			@JsonProperty("peopleNm")
			private String peopleNm;
			@JsonProperty("peopleNmEn")
			private String peopleNmEn;
			@JsonProperty("cast")
			private String cast;
			@JsonProperty("castEn")
			private String castEn;
		}

		@Data
		public static class ShowType {
			@JsonProperty("showTypeGroupNm")
			private String showTypeGroupNm;
			@JsonProperty("showTypeNm")
			private String showTypeNm;
		}

		@Data
		public static class Company {
			@JsonProperty("companyCd")
			private String companyCd;
			@JsonProperty("companyNm")
			private String companyNm;
			@JsonProperty("companyNmEn")
			private String companyNmEn;
			@JsonProperty("companyPartNm")
			private String companyPartNm;
		}

		@Data
		public static class Audit {
			@JsonProperty("auditNo")
			private String auditNo;
			@JsonProperty("watchGradeNm")
			private String watchGradeNm;
		}

		@Data
		public static class Staff {
			@JsonProperty("peopleNm")
			private String peopleNm;
			@JsonProperty("peopleNmEn")
			private String peopleNmEn;
			@JsonProperty("staffRoleNm")
			private String staffRoleNm;
		}
	}
	
	private String imgPath;
	private Integer hitno;
	private String salesAcc;
	private String audiAcc;
	private String uploadCheck;
	
	/*
	// 엔티티를 받아서 DTO 객체를 초기화하는 생성자
	public KobisMovieDTO(MovieEntity movieEntity) {
		this.imgPath = movieEntity.getImgPath();
		this.hitno = movieEntity.getHitno();
		this.salesAcc = movieEntity.getSalesAcc();
		this.audiAcc = movieEntity.getAudiAcc();
		this.uploadCheck = movieEntity.getUploadCheck();
		
		 // MovieInfo 설정
        this.movieInfoResult = new MovieInfoResult();
        this.movieInfoResult.setMovieInfo(new MovieInfo());

        // MovieInfo의 필드 값들 설정
        MovieInfo movieInfo = this.movieInfoResult.getMovieInfo();
        movieInfo.setMovieCd(movieEntity.getMovieCd());
        movieInfo.setMovieNm(movieEntity.getMovieNm());
        movieInfo.setMovieNmEn(movieEntity.getMovieNmEn());
        movieInfo.setPrdtYear(movieEntity.getPrdtYear());
        movieInfo.setOpenDt(movieEntity.getOpenDt());
        movieInfo.setOverview(movieEntity.getOverview());
        movieInfo.setPosterPath(movieEntity.getPosterPath());
        movieInfo.setShowTm(movieEntity.getShowtime());
        
        // watchGradeNm 은 배열이라 이렇게 처리함.
        movieInfo.getAudits().get(0).setWatchGradeNm(movieEntity.getWatchGradeNm());
        
        // nations 처리
        if (movieEntity.getNations() != null && !movieEntity.getNations().isEmpty()) {
            List<MovieInfo.Nation> nationsList = new ArrayList<>();
            for (NationEntity nationEntity : movieEntity.getNations()) {
                MovieInfo.Nation nationDTO = new MovieInfo.Nation();
                nationDTO.setNationNm(nationEntity.getNationNm()); // NationEntity의 nationNm을 MovieInfo.Nation에 설정
                nationsList.add(nationDTO);
            }
            movieInfo.setNations(nationsList);
        }

        // companys 처리
        if (movieEntity.getCompanys() != null && !movieEntity.getCompanys().isEmpty()) {
            List<MovieInfo.Company> companyList = new ArrayList<>();
            for (CompanyEntity companyEntity : movieEntity.getCompanys()) {
                MovieInfo.Company companyDTO = new MovieInfo.Company();
                companyDTO.setCompanyCd(companyEntity.getCompanyCd());
                companyDTO.setCompanyNm(companyEntity.getCompanyNm());
                companyDTO.setCompanyNmEn(companyEntity.getCompanyNmEn());
                companyDTO.setCompanyPartNm(companyEntity.getCompanyPartNm());
                companyList.add(companyDTO);
            }
            movieInfo.setCompanys(companyList);
        }

        // genres 처리
        if (movieEntity.getGenres() != null && !movieEntity.getGenres().isEmpty()) {
            List<MovieInfo.Genre> genreList = new ArrayList<>();
            for (GenreEntity genreEntity : movieEntity.getGenres()) {
                MovieInfo.Genre genreDTO = new MovieInfo.Genre();
                genreDTO.setGenreNm(genreEntity.getGenreNm());
                genreList.add(genreDTO);
            }
            movieInfo.setGenres(genreList);
        }

        // actors 처리
        if (movieEntity.getActors() != null && !movieEntity.getActors().isEmpty()) {
            List<MovieInfo.Actor> actorList = new ArrayList<>();
            for (ActorEntity actorEntity : movieEntity.getActors()) {
                MovieInfo.Actor actorDTO = new MovieInfo.Actor();
                actorDTO.setPeopleNm(actorEntity.getPeopleNm());
                actorDTO.setPeopleNmEn(actorEntity.getPeopleNmEn());
                actorDTO.setCast(actorEntity.getCast());
                actorDTO.setCastEn(actorEntity.getCastEn());
                actorList.add(actorDTO);
            }
            movieInfo.setActors(actorList);
        }

        // directors 처리
        if (movieEntity.getDirectors() != null && !movieEntity.getDirectors().isEmpty()) {
            List<MovieInfo.Director> directorList = new ArrayList<>();
            for (DirectorEntity directorEntity : movieEntity.getDirectors()) {
                MovieInfo.Director directorDTO = new MovieInfo.Director();
                directorDTO.setPeopleNm(directorEntity.getPeopleNm());
                directorDTO.setPeopleNmEn(directorEntity.getPeopleNmEn());
                directorList.add(directorDTO);
            }
            movieInfo.setDirectors(directorList);
        }
    }
	

	public MovieEntity dtoToEntity(KobisMovieDTO dto) {
	    // MovieEntity 객체 생성
	    MovieEntity movieEntity = new MovieEntity();  // 기본 생성자를 사용

	    // 일반적인 setter를 사용해서 필드 값 설정
	    movieEntity.setImgPath(dto.getImgPath());
	    movieEntity.setHitno(dto.getHitno());
	    movieEntity.setSalesAcc(dto.getSalesAcc());
	    movieEntity.setAudiAcc(dto.getAudiAcc());
	    movieEntity.setUploadCheck(dto.getUploadCheck());

	    // MovieInfo에서 가져온 필드들을 MovieEntity에 설정
	    if (dto.getMovieInfoResult() != null && dto.getMovieInfoResult().getMovieInfo() != null) {
	        KobisMovieDTO.MovieInfo movieInfo = dto.getMovieInfoResult().getMovieInfo();

	        // MovieInfo의 필드 값들을 MovieEntity에 설정
	        movieEntity.setMovieCd(movieInfo.getMovieCd());
	        movieEntity.setMovieNm(movieInfo.getMovieNm());
	        movieEntity.setMovieNmEn(movieInfo.getMovieNmEn());
	        movieEntity.setShowtime(movieInfo.getShowTm());
	        movieEntity.setPrdtYear(movieInfo.getPrdtYear());
	        movieEntity.setOpenDt(movieInfo.getOpenDt());
	        movieEntity.setOverview(movieInfo.getOverview());
	        movieEntity.setPosterPath(movieInfo.getPosterPath());

	        // watchGradeNm 은 배열이라 이렇게 처리함.
	        String watchGradeNm = movieInfo.getAudits().get(0).getWatchGradeNm();
	        movieEntity.setWatchGradeNm(watchGradeNm);
	        
	        // nations 필드 처리
	        if (movieInfo.getNations() != null) {
	            for (KobisMovieDTO.MovieInfo.Nation nationDTO : movieInfo.getNations()) {
	                NationEntity nationEntity = new NationEntity();
	                nationEntity.setNationNm(nationDTO.getNationNm());
	                movieEntity.addNations(nationEntity);
	            }
	        }

	        // companys 필드 처리
	        if (movieInfo.getCompanys() != null) {
	            for (KobisMovieDTO.MovieInfo.Company companyDTO : movieInfo.getCompanys()) {
	                CompanyEntity companyEntity = new CompanyEntity();
	                companyEntity.setCompanyNm(companyDTO.getCompanyNm());
	                companyEntity.setCompanyCd(companyDTO.getCompanyCd());
	                companyEntity.setCompanyPartNm(companyDTO.getCompanyPartNm());
	                movieEntity.addCompanys(companyEntity);
	            }
	        }

	        // genres 필드 처리
	        if (movieInfo.getGenres() != null) {
	            for (KobisMovieDTO.MovieInfo.Genre genreDTO : movieInfo.getGenres()) {
	                GenreEntity genreEntity = new GenreEntity();
	                genreEntity.setGenreNm(genreDTO.getGenreNm());
	                movieEntity.addGenres(genreEntity);
	            }
	        }

	        // actors 필드 처리
	        if (movieInfo.getActors() != null) {
	            for (KobisMovieDTO.MovieInfo.Actor actorDTO : movieInfo.getActors()) {
	                ActorEntity actorEntity = new ActorEntity();
	                actorEntity.setPeopleNm(actorDTO.getPeopleNm());
	                actorEntity.setCast(actorDTO.getCast());
	                actorEntity.setPeopleNmEn(actorDTO.getPeopleNmEn());
	                actorEntity.setCastEn(actorDTO.getCastEn());
	                movieEntity.addActors(actorEntity);
	            }
	        }

	        // directors 필드 처리
	        if (movieInfo.getDirectors() != null) {
	            for (KobisMovieDTO.MovieInfo.Director directorDTO : movieInfo.getDirectors()) {
	                DirectorEntity directorEntity = new DirectorEntity();
	                directorEntity.setPeopleNm(directorDTO.getPeopleNm());
	                directorEntity.setPeopleNmEn(directorDTO.getPeopleNmEn());
	                movieEntity.addDirectors(directorEntity);
	            }
	        }

	    }

	    // MovieEntity 반환
	    return movieEntity;
	}
    	
*/
	
}
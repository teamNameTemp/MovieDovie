package com.board.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

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

@Component
public class MovieMapper {
	
    public List<MovieEntity> toEntityList(List<MovieDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public MovieEntity toEntity(MovieDTO dto) {
        if (dto == null) {
            return null;
        }
        MovieEntity entity = MovieEntity.builder()
                .movieId(dto.getId())
                .title(dto.getTitle())
                .originalTitle(dto.getOriginalTitle())
                .tagline(dto.getTagline())
                .overview(dto.getOverview())
                .runtime(dto.getRuntime())
                .backdropPath(dto.getBackdropPath())
                .posterPath(dto.getPosterPath())
                .originalLanguage(dto.getOriginalLanguage())
                .imdbId(dto.getImdbId())
                .popularity(dto.getPopularity())
                .releaseDate(dto.getReleaseDate())
                .voteAverage(dto.getVoteAverage())
                .voteCount(dto.getVoteCount())
                .status(dto.getStatus())
                .certification(getCertification(dto.getReleaseDates()))
                .build();
        
        dto.setEntity(entity);
        return entity;
    }

    public List<CompanyEntity> mapProductionCompanies(MovieDTO dto) {
        if (dto.getProductionCompanies() == null) return new ArrayList<>();
        
        return dto.getProductionCompanies().stream()
                .map(company -> CompanyEntity.builder()
                    .movieId(dto.getEntity())
                    .companyId(company.getId())
                    .logoPath(company.getLogoPath())
                    .companyNm(company.getName())
                    .originCountry(company.getOriginCountry())
                    .build())
                .collect(Collectors.toList());
        	
    }
    
//    private List<CompanyEntity> mapProductionCompanies(List<MovieDTO.ProductionCompany> dtos) {
//        if (dtos == null) return null;
//        return dtos.stream().map(dto -> {
//        	return CompanyEntity.builder()
//        			.companyId(dto.getId())
//        			.logoPath(dto.getLogoPath())
//        			.companyNm(dto.getName())
//        			.build();
//        	
//        }).collect(Collectors.toList());
//    }

    public List<CountryEntity> mapProductionCountries(MovieDTO dto) {
        if (dto.getProductionCountries() == null) return new ArrayList<>();
        return dto.getProductionCountries().stream().map(country -> {
        	return CountryEntity.builder()
        			.movieId(dto.getEntity())
        			.iso3166_1(country.getIso3166_1())
        			.countryNm(country.getName())
        			.build();
        	
        }).collect(Collectors.toList());
    }

    public List<LanguageEntity> mapSpokenLanguages(MovieDTO dto) {
        if (dto.getSpokenLanguages() == null) return new ArrayList<>();
        return dto.getSpokenLanguages().stream().map(language -> {
        	return LanguageEntity.builder()
        			.movieId(dto.getEntity())
        			.englishNm(language.getEnglishName())
        			.iso639_1(language.getIso639_1())
        			.languageNm(language.getName())
        			.build();
        }).collect(Collectors.toList());
    }


    public List<GenreEntity> mapGenres(MovieDTO dto) {
        if (dto.getGenres() == null) return new ArrayList<>();
        return dto.getGenres().stream().map(genre -> {
        	return GenreEntity.builder()
        			.movieId(dto.getEntity())
        			.genreid(genre.getId())
        			.genreNm(genre.getName())
        			.build();
        }).collect(Collectors.toList());
    }

    public List<VideoEntity> mapVideos(MovieDTO dto) {
        if (dto.getVideos() == null) return null;
        return dto.getVideos().getResults().stream().map(video->{
        	return VideoEntity.builder()
        			.movieId(dto.getEntity())
        			.videoId(video.getId())
        			.iso3166_1(video.getIso3166_1())
        			.iso639_1(video.getIso639_1())
        			.videoKey(video.getKey())
        			.videoNm(video.getName())
        			.videoSite(video.getSite())
        			.videoSize(video.getSize())
        			.videoType(video.getType())
        			.build();
        }).collect(Collectors.toList());
    }

   

    private String getCertification(MovieDTO.ReleaseDates dto) {
        if (dto == null) return "";
        String certi = "";
        
        for (MovieDTO.ReleaseDateInfo info : dto.getResults()) {
            if (info.getIso31661().equals("KR")) {
                if (!info.getReleaseDates().isEmpty()) {
                    certi = info.getReleaseDates().get(0).getCertification();
                    break;
                }
            }
        }
        return certi;
    }
    
    public CreditEntity mapCredits(MovieDTO dto) {
        if (dto.getCredits() == null) return null;
        CreditEntity entity = new CreditEntity();
        entity.setMovieId(dto.getEntity());
        
        dto.getCredits().setCredit(entity);
        
        return entity;
    }
    
    public List<CrewEntity> mapCrews(MovieDTO.Credits dto) {
    	if(dto == null)return new ArrayList<>();
    	return dto.getCrew().stream().map(crew->{
    		return CrewEntity.builder()
    				.creditId(dto.getCredit())
    				.crewId(crew.getId())
    				.crewNm(crew.getName())
    				.jobTitle(crew.getJobTitle())
    				.department(crew.getDepartment())
    				.profilePath(crew.getProfilePath())
    				.build();
    	}).collect(Collectors.toList());
    }
    
    public List<CastEntity> mapCasts(MovieDTO.Credits dto) {
    	if(dto == null)return new ArrayList<>();
    	return dto.getCast().stream().map(cast->{
    		return CastEntity.builder()
    				.creditId(dto.getCredit())
    				.castId(cast.getId())
    				.castNm(cast.getName())
    				.characterNm(cast.getCharacterName())
    				.profilePath(cast.getProfilePath())
    				.castOrd(cast.getOrder())
    				.build();
    	}).collect(Collectors.toList());
    }

    
//    private List<BriefEntity> mapMovieBriefs(List<MovieDTO.MovieBrief> dtos) {
//        if (dtos == null) return null;
//        return dtos.stream().map(dto -> {
//            MovieEntity.BriefEntity entity = new MovieEntity.BriefEntity();
//            entity.setId(dto.getId());
//            entity.setTitle(dto.getTitle());
//            entity.setPosterPath(dto.getPosterPath());
//            entity.setReleaseDate(dto.getReleaseDate());
//            return entity;
//        }).collect(Collectors.toList());
//    }
}
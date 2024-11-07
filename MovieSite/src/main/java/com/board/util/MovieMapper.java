package com.board.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.MovieEntity;

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

        return MovieEntity.builder()
                .id(dto.getId())
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
                .productionCompanies(mapProductionCompanies(dto.getProductionCompanies()))
                .productionCountries(mapProductionCountries(dto.getProductionCountries()))
                .spokenLanguages(mapSpokenLanguages(dto.getSpokenLanguages()))
                .status(dto.getStatus())
                .video(dto.getVideo())
                .belongsToCollection(mapCollectionInfo(dto.getBelongsToCollection()))
                .genres(mapGenres(dto.getGenres()))
                .adult(dto.isAdult())
                .budget(dto.getBudget())
                .homepage(dto.getHomepage())
                .revenue(dto.getRevenue())
                .keywords(mapKeywords(dto.getKeywords()))
                .videos(mapVideos(dto.getVideos()))
                .images(mapImages(dto.getImages()))
                .releaseDates(mapReleaseDates(dto.getReleaseDates()))
                .credits(mapCredits(dto.getCredits()))
                .reviews(mapReviews(dto.getReviews()))
                .similarMovies(mapMovieBriefs(dto.getSimilarMovies()))
                .recommendedMovies(mapMovieBriefs(dto.getRecommendedMovies()))
                .build();
    }

    private List<MovieEntity.ProductionCompanyEntity> mapProductionCompanies(List<MovieDTO.ProductionCompany> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.ProductionCompanyEntity entity = new MovieEntity.ProductionCompanyEntity();
            entity.setId(dto.getId());
            entity.setLogoPath(dto.getLogoPath());
            entity.setName(dto.getName());
            entity.setOriginCountry(dto.getOriginCountry());
            return entity;
        }).collect(Collectors.toList());
    }

    private List<MovieEntity.ProductionCountryEntity> mapProductionCountries(List<MovieDTO.ProductionCountry> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.ProductionCountryEntity entity = new MovieEntity.ProductionCountryEntity();
            entity.setIso3166_1(dto.getIso3166_1());
            entity.setName(dto.getName());
            return entity;
        }).collect(Collectors.toList());
    }

    private List<MovieEntity.SpokenLanguageEntity> mapSpokenLanguages(List<MovieDTO.SpokenLanguage> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.SpokenLanguageEntity entity = new MovieEntity.SpokenLanguageEntity();
            entity.setEnglishName(dto.getEnglishName());
            entity.setIso639_1(dto.getIso639_1());
            entity.setName(dto.getName());
            return entity;
        }).collect(Collectors.toList());
    }

    private MovieEntity.CollectionInfoEntity mapCollectionInfo(MovieDTO.CollectionInfo dto) {
        if (dto == null) return null;
        MovieEntity.CollectionInfoEntity entity = new MovieEntity.CollectionInfoEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPosterPath(dto.getPosterPath());
        entity.setBackdropPath(dto.getBackdropPath());
        return entity;
    }

    private List<MovieEntity.GenreEntity> mapGenres(List<MovieDTO.Genre> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.GenreEntity entity = new MovieEntity.GenreEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            return entity;
        }).collect(Collectors.toList());
    }

    private MovieEntity.KeywordsEntity mapKeywords(MovieDTO.Keywords dto) {
        if (dto == null) return null;
        MovieEntity.KeywordsEntity entity = new MovieEntity.KeywordsEntity();
        entity.setKeywords(dto.getKeywords().stream().map(keyword -> {
            MovieEntity.KeywordEntity keywordEntity = new MovieEntity.KeywordEntity();
            keywordEntity.setId(keyword.getId());
            keywordEntity.setName(keyword.getName());
            return keywordEntity;
        }).collect(Collectors.toList()));
        return entity;
    }

    private MovieEntity.VideosEntity mapVideos(MovieDTO.Videos dto) {
        if (dto == null) return null;
        MovieEntity.VideosEntity entity = new MovieEntity.VideosEntity();
        entity.setResults(dto.getResults().stream().map(video -> {
            MovieEntity.VideoEntity videoEntity = new MovieEntity.VideoEntity();
            videoEntity.setId(video.getId());
            videoEntity.setIso639_1(video.getIso639_1());
            videoEntity.setIso3166_1(video.getIso3166_1());
            videoEntity.setVideoKey(video.getKey());
            videoEntity.setName(video.getName());
            videoEntity.setSite(video.getSite());
            videoEntity.setVideoSize(video.getSize());
            videoEntity.setVideoType(video.getType());
            return videoEntity;
        }).collect(Collectors.toList()));
        return entity;
    }

    private MovieEntity.ImagesEntity mapImages(MovieDTO.Images dto) {
        if (dto == null) return null;
        MovieEntity.ImagesEntity entity = new MovieEntity.ImagesEntity();
        entity.setBackdrops(mapImageList(dto.getBackdrops()));
        entity.setPosters(mapImageList(dto.getPosters()));
        return entity;
    }

    private List<MovieEntity.ImageEntity> mapImageList(List<MovieDTO.Image> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.ImageEntity entity = new MovieEntity.ImageEntity();
            entity.setAspectRatio(dto.getAspectRatio());
            entity.setFilePath(dto.getFilePath());
            entity.setHeight(dto.getHeight());
            entity.setIso639_1(dto.getIso639_1());
            entity.setVoteAverage(dto.getVoteAverage());
            entity.setVoteCount(dto.getVoteCount());
            entity.setWidth(dto.getWidth());
            return entity;
        }).collect(Collectors.toList());
    }

    private MovieEntity.ReleaseDatesEntity mapReleaseDates(MovieDTO.ReleaseDates dto) {
        if (dto == null) return null;
        MovieEntity.ReleaseDatesEntity entity = new MovieEntity.ReleaseDatesEntity();
        entity.setResults(dto.getResults().stream().map(result -> {
            MovieEntity.ReleaseDateInfoEntity infoEntity = new MovieEntity.ReleaseDateInfoEntity();
            infoEntity.setIso31661(result.getIso31661());
            infoEntity.setReleaseDates(result.getReleaseDates().stream().map(date -> {
                MovieEntity.ReleaseDateEntity dateEntity = new MovieEntity.ReleaseDateEntity();
                dateEntity.setCertification(date.getCertification());
                dateEntity.setReleaseType(date.getType());
                dateEntity.setReleaseDate(date.getReleaseDate());
                return dateEntity;
            }).collect(Collectors.toList()));
            return infoEntity;
        }).collect(Collectors.toList()));
        return entity;
    }

    private MovieEntity.CreditsEntity mapCredits(MovieDTO.Credits dto) {
        if (dto == null) return null;
        MovieEntity.CreditsEntity entity = new MovieEntity.CreditsEntity();
        entity.setCast(dto.getCast().stream().map(cast -> {
            MovieEntity.CastEntity castEntity = new MovieEntity.CastEntity();
            castEntity.setId(cast.getId());
            castEntity.setName(cast.getName());
            castEntity.setCharacterName(cast.getCharacterName());
            castEntity.setProfilePath(cast.getProfilePath());
            castEntity.setCastOrder(cast.getOrder());
            return castEntity;
        }).collect(Collectors.toList()));
        entity.setCrew(dto.getCrew().stream().map(crew -> {
            MovieEntity.CrewEntity crewEntity = new MovieEntity.CrewEntity();
            crewEntity.setId(crew.getId());
            crewEntity.setName(crew.getName());
            crewEntity.setJobTitle(crew.getJobTitle());
            crewEntity.setDepartment(crew.getDepartment());
            crewEntity.setProfilePath(crew.getProfilePath());
            return crewEntity;
        }).collect(Collectors.toList()));
        return entity;
    }

    private List<MovieEntity.ReviewEntity> mapReviews(List<MovieDTO.Review> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.ReviewEntity entity = new MovieEntity.ReviewEntity();
            entity.setId(dto.getId());
            entity.setAuthor(dto.getAuthor());
            entity.setContent(dto.getContent());
            entity.setUrl(dto.getUrl());
            return entity;
        }).collect(Collectors.toList());
    }

    private List<MovieEntity.MovieBriefEntity> mapMovieBriefs(List<MovieDTO.MovieBrief> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            MovieEntity.MovieBriefEntity entity = new MovieEntity.MovieBriefEntity();
            entity.setId(dto.getId());
            entity.setTitle(dto.getTitle());
            entity.setPosterPath(dto.getPosterPath());
            entity.setReleaseDate(dto.getReleaseDate());
            return entity;
        }).collect(Collectors.toList());
    }
}
package com.board.dto;

import java.time.LocalDate;
import java.util.List;

import com.board.entity.UploadedMovieEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UploadedMovieDTO {

    private Long id;								//기본키
    private String backdropPath;					//뒷 배경
    private String posterPath;						//포스터 사진
    private String title;							//제목 (한국어)
    private String originalTitle;					//원제
    private LocalDate releaseDate;					//발매일
    private String originalLanguage;				//원어
    private Integer runtime;						//상영시간 (분)
    private String overview;						//줄거리
    private int hitno;								//상세페이지 클릭 수
    		
    //private CollectionInfo belongsToCollection;
    private List<Genre> genres;
    private List<ProductionCompany> productionCompanies;
    private List<ProductionCountry> productionCountries;
    private List<SpokenLanguage> spokenLanguages;

    // 1. 엔티티를 받아서 DTO 객체를 초기화하는 생성자
    public UploadedMovieDTO(UploadedMovieEntity entity) {
        this.id = entity.getId();
        this.backdropPath = entity.getBackdropPath();
        this.posterPath = entity.getPosterPath();
        this.title = entity.getTitle();
        this.originalTitle = entity.getOriginalTitle();
        this.releaseDate = entity.getReleaseDate();
        this.originalLanguage = entity.getOriginalLanguage();
        this.runtime = entity.getRuntime();
        this.overview = entity.getOverview();
        this.hitno = entity.getHitno();
        //this.belongsToCollection = new CollectionInfo(entity.getBelongsToCollection());
       // this.genres = entity.getGenres();
      //  this.productionCompanies = entity.getProductionCompanies();
       // this.productionCountries = entity.getProductionCountries();
       // this.spokenLanguages = entity.getSpokenLanguages();
    }

    // 2. dtoToEntity 메서드
    public UploadedMovieEntity dtoToEntity() {
        return UploadedMovieEntity.builder()
					                .id(this.id)
					                .backdropPath(this.backdropPath)
					                .posterPath(this.posterPath)
					                .title(this.title)
					                .originalTitle(this.originalTitle)
					                .releaseDate(this.releaseDate)
					                .originalLanguage(this.originalLanguage)
					                .runtime(this.runtime)
					                .overview(this.overview)
					                .hitno(this.hitno)
					                //.belongsToCollection(this.belongsToCollection.toEntity())
					            //    .genres(this.genres)
					             //   .productionCompanies(this.productionCompanies)
					           //     .productionCountries(this.productionCountries)
					           //     .spokenLanguages(this.spokenLanguages)
					                .build();
    }

    /*
    @Data
    public static class CollectionInfo {
        private int id;
        private String name;
        private String posterPath;
        private String backdropPath;

        public CollectionInfo(Collection entity) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.posterPath = entity.getPosterPath();
            this.backdropPath = entity.getBackdropPath();
        }

        public Collection toEntity() {
            return Collection.builder()
                    .id(this.id)
                    .name(this.name)
                    .posterPath(this.posterPath)
                    .backdropPath(this.backdropPath)
                    .build();
        }
    }
    */

    @Data
    public static class ProductionCompany {
        private int id;
        private String logoPath;
        private String name;
        private String originCountry;
    }

    @Data
    public static class ProductionCountry {
        private String iso3166_1;
        private String name;
    }

    @Data
    public static class Genre {
        private int id;
        private String name;
    }

    @Data
    public static class SpokenLanguage {
        private String englishName;
        private String iso639_1;
        private String name;
    }
}

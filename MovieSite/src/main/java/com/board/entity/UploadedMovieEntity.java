package com.board.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UploadedMovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String posterPath;
    private String backdropPath;
    private String title;
    private String originalTitle;
    private LocalDate releaseDate;
    private String originalLanguage;
    private Integer runtime;
    private String overview;
    private int hitno;
    private String certification; //관람 등급

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "collection_id")
    private Collection belongsToCollection;
	*/
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "movie_genre",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "movie_production_company",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<ProductionCompany> productionCompanies;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "movie_production_country",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "country_code")
    )
    private List<ProductionCountry> productionCountries;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "movie_spoken_language",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "language_code")
    )
    private List<SpokenLanguage> spokenLanguages;

    
  
    // 하위 엔티티 클래스들
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Genre {
    	
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductionCompany {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String logoPath;
        private String name;
        private String originCountry;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductionCountry {
        @Id
        @Column(name = "iso3166_1")
        private String iso3166_1;
        
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpokenLanguage {
        @Id
        @Column(name = "iso639_1")
        private String iso639_1;
        
        private String englishName;
        private String name;
    }
    


    /* 
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "collection")
    public static class Collection {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;
        private String posterPath;
        private String backdropPath;
    }
    */
}

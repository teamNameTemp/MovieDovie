package com.board.entity.movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MOVIES")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")
    @SequenceGenerator(name = "MOVIE_SEQ", sequenceName = "MOVIE_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "ORIGINAL_TITLE")
    private String originalTitle;

    private String tagline;

    @Column(columnDefinition = "CLOB")
    private String overview;

    private Integer runtime;

    @Column(name = "BACKDROP_PATH")
    private String backdropPath;

    @Column(name = "POSTER_PATH")
    private String posterPath;

    @Column(name = "ORIGINAL_LANGUAGE")
    private String originalLanguage;

    @Column(name = "IMDB_ID")
    private String imdbId;

    private Double popularity;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Column(name = "VOTE_AVERAGE")
    private Double voteAverage;

    @Column(name = "VOTE_COUNT")
    private Integer voteCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MOVIE_ID")
    private List<ProductionCompanyEntity> productionCompanies;

    @ElementCollection
    @CollectionTable(name = "PRODUCTION_COUNTRIES", joinColumns = @JoinColumn(name = "MOVIE_ID"))
    private List<ProductionCountryEntity> productionCountries;

    @ElementCollection
    @CollectionTable(name = "SPOKEN_LANGUAGES", joinColumns = @JoinColumn(name = "MOVIE_ID"))
    private List<SpokenLanguageEntity> spokenLanguages;

    private String status;

    private Boolean video;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COLLECTION_ID")
    private CollectionInfoEntity belongsToCollection;

    @ManyToMany
    @JoinTable(
        name = "MOVIE_GENRES",
        joinColumns = @JoinColumn(name = "MOVIE_ID"),
        inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
    )
    private List<GenreEntity> genres;

    private Boolean adult;

    private Long budget;

    private String homepage;

    private Long revenue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "KEYWORDS_ID")
    private KeywordsEntity keywords;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "VIDEOS_ID")
    private VideosEntity videos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IMAGES_ID")
    private ImagesEntity images;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RELEASE_DATES_ID")
    private ReleaseDatesEntity releaseDates;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CREDITS_ID")
    private CreditsEntity credits;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MOVIE_ID")
    private List<ReviewEntity> reviews;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MOVIE_ID")
    private List<MovieBriefEntity> similarMovies;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MOVIE_ID")
    private List<MovieBriefEntity> recommendedMovies;

    // Nested entities
    @Entity
    @Table(name = "PRODUCTION_COMPANIES")
    @Data
    public static class ProductionCompanyEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROD_COMP_SEQ")
        @SequenceGenerator(name = "PROD_COMP_SEQ", sequenceName = "PROD_COMP_SEQ", allocationSize = 1)
        private Long id;
        
        @Column(name = "LOGO_PATH")
        private String logoPath;
        
        private String name;
        
        @Column(name = "ORIGIN_COUNTRY")
        private String originCountry;
    }

    @Embeddable
    @Data
    public static class ProductionCountryEntity {
        @Column(name = "ISO_3166_1")
        private String iso3166_1;
        private String name;
    }

    @Embeddable
    @Data
    public static class SpokenLanguageEntity {
        @Column(name = "ENGLISH_NAME")
        private String englishName;
        
        @Column(name = "ISO_639_1")
        private String iso639_1;
        
        private String name;
    }

    @Entity
    @Table(name = "COLLECTIONS")
    @Data
    public static class CollectionInfoEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COLLECTION_SEQ")
        @SequenceGenerator(name = "COLLECTION_SEQ", sequenceName = "COLLECTION_SEQ", allocationSize = 1)
        private Long id;
        
        private String name;
        
        @Column(name = "POSTER_PATH")
        private String posterPath;
        
        @Column(name = "BACKDROP_PATH")
        private String backdropPath;
    }

    @Entity
    @Table(name = "GENRES")
    @Data
    public static class GenreEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENRE_SEQ")
        @SequenceGenerator(name = "GENRE_SEQ", sequenceName = "GENRE_SEQ", allocationSize = 1)
        private Long seqno;
        
        private Long id;
        private String name;
    }

    @Entity
    @Table(name = "KEYWORDS")
    @Data
    public static class KeywordsEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KEYWORDS_SEQ")
        @SequenceGenerator(name = "KEYWORDS_SEQ", sequenceName = "KEYWORDS_SEQ", allocationSize = 1)
        private Long id;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "KEYWORDS_ID")
        private List<KeywordEntity> keywords;
    }

    @Entity
    @Table(name = "KEYWORD")
    @Data
    public static class KeywordEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KEYWORD_SEQ")
        @SequenceGenerator(name = "KEYWORD_SEQ", sequenceName = "KEYWORD_SEQ", allocationSize = 1)
        private Long id;
        
        private String name;
    }

    @Entity
    @Table(name = "VIDEOS")
    @Data
    public static class VideosEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VIDEOS_SEQ")
        @SequenceGenerator(name = "VIDEOS_SEQ", sequenceName = "VIDEOS_SEQ", allocationSize = 1)
        private Long id;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "VIDEOS_ID")
        private List<VideoEntity> results;
    }

    @Entity
    @Table(name = "VIDEO")
    @Data
    public static class VideoEntity {
        @Id
        private String id;
        
        @Column(name = "ISO_639_1")
        private String iso639_1;
        
        @Column(name = "ISO_3166_1")
        private String iso3166_1;
        
        @Column(name = "VIDEO_KEY")
        private String videoKey;
        
        private String name;
        private String site;
        private Integer videoSize;
        
        @Column(name = "VIDEO_TYPE")
        private String videoType;
    }

    @Entity
    @Table(name = "IMAGES")
    @Data
    public static class ImagesEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGES_SEQ")
        @SequenceGenerator(name = "IMAGES_SEQ", sequenceName = "IMAGES_SEQ", allocationSize = 1)
        private Long id;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "IMAGES_ID")
        private List<ImageEntity> backdrops;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "IMAGES_ID")
        private List<ImageEntity> posters;
    }

    @Entity
    @Table(name = "IMAGE")
    @Data
    public static class ImageEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGE_SEQ")
        @SequenceGenerator(name = "IMAGE_SEQ", sequenceName = "IMAGE_SEQ", allocationSize = 1)
        private Long id;
        
        @Column(name = "ASPECT_RATIO")
        private Double aspectRatio;
        
        @Column(name = "FILE_PATH")
        private String filePath;
        
        private Integer height;
        
        @Column(name = "ISO_639_1")
        private String iso639_1;
        
        @Column(name = "VOTE_AVERAGE")
        private Double voteAverage;
        
        @Column(name = "VOTE_COUNT")
        private Integer voteCount;
        
        private Integer width;
    }

    @Entity
    @Table(name = "RELEASE_DATES")
    @Data
    public static class ReleaseDatesEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASE_DATES_SEQ")
        @SequenceGenerator(name = "RELEASE_DATES_SEQ", sequenceName = "RELEASE_DATES_SEQ", allocationSize = 1)
        private Long id;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "RELEASE_DATES_ID")
        private List<ReleaseDateInfoEntity> results;
    }

    @Entity
    @Table(name = "RELEASE_DATE_INFO")
    @Data
    public static class ReleaseDateInfoEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASE_DATE_INFO_SEQ")
        @SequenceGenerator(name = "RELEASE_DATE_INFO_SEQ", sequenceName = "RELEASE_DATE_INFO_SEQ", allocationSize = 1)
        private Long id;
        
        @Column(name = "ISO_3166_1")
        private String iso31661;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "RELEASE_DATE_INFO_ID")
        private List<ReleaseDateEntity> releaseDates;
    }

    @Entity
    @Table(name = "RELEASE_DATE")
    @Data
    public static class ReleaseDateEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASE_DATE_SEQ")
        @SequenceGenerator(name = "RELEASE_DATE_SEQ", sequenceName = "RELEASE_DATE_SEQ", allocationSize = 1)
        private Long id;
        
        private String certification;
        
        @Column(name = "RELEASE_TYPE")
        private String releaseType;
        
        @Column(name = "RELEASE_DATE")
        private String releaseDate;
    }

    @Entity
    @Table(name = "CREDITS")
    @Data
    public static class CreditsEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDITS_SEQ")
        @SequenceGenerator(name = "CREDITS_SEQ", sequenceName = "CREDITS_SEQ", allocationSize = 1)
        private Long id;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "CREDITS_ID")
        private List<CastEntity> cast;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "CREDITS_ID")
        private List<CrewEntity> crew;
    }

    @Entity
    @Table(name = "CAST")
    @Data
    public static class CastEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAST_SEQ")
        @SequenceGenerator(name = "CAST_SEQ", sequenceName = "CAST_SEQ", allocationSize = 1)
        private Long id;
        
        private String name;
        
        @Column(name = "CHARACTER_NAME")
        private String characterName;
        
        @Column(name = "PROFILE_PATH")
        private String profilePath;
        
        @Column(name = "CAST_ORDER")
        private Integer castOrder;
    }

    @Entity
    @Table(name = "CREW")
    @Data
    public static class CrewEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREW_SEQ")
        @SequenceGenerator(name = "CREW_SEQ", sequenceName = "CREW_SEQ", allocationSize = 1)
        private Long id;
        
        private String name;
        
        @Column(name = "JOB_TITLE")
        private String jobTitle;
        
        private String department;
        
        @Column(name = "PROFILE_PATH")
        private String profilePath;
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CrewEntity that = (CrewEntity) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    @Entity
    @Table(name = "REVIEWS")
    @Data
    public static class ReviewEntity {
        @Id
        private String id;
        
        private String author;
        
        @Column(columnDefinition = "CLOB")
        private String content;
        
        private String url;
    }

    @Entity
    @Table(name = "MOVIE_BRIEF")
    @Data
    public static class MovieBriefEntity {
        @Id
        private Long id;
        
        private String title;
        
        @Column(name = "POSTER_PATH")
        private String posterPath;
        
        @Column(name = "RELEASE_DATE")
        private LocalDate releaseDate;
    }
}
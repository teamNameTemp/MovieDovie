package com.board.entity.movie;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovieEntity {

//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")
//    @SequenceGenerator(name = "MOVIE_SEQ", sequenceName = "movie_seq", allocationSize = 1)
//    private Long seqno;
    @Id
    private Long movieId;

    @Column(name="adult")
    private Boolean isAdult;
    
    @Column(nullable = false)
    private String title;

    @Column(name = "ORIGINAL_TITLE")
    private String originalTitle;

    private String tagline;

    @Column(name = "overview", length=2000)
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

    private String status;
    
    private String certification;
    
    private String uploadCheck;
    
    private int price;
    
}
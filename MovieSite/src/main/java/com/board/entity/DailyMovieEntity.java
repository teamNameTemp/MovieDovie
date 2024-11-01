package com.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="dailyMovie")
@Table(name = "tbl_daily_movie")
public class DailyMovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="rank",nullable=true)
    private String rank;
    
    @Column(name = "movie_nm",nullable=true)
    private String movieNm;

    @Column(name = "prdt_year",nullable=true)
    private String prdtYear;

    @Column(name = "poster_path",nullable=true)
    private String posterPath;

    @Column(name = "overview", length = 1000,nullable=true)
    private String overview;

    @Column(name = "tmdb_id",nullable=true)
    private Integer tmdbId;

    // Getters and setters
}

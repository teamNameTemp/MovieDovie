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

    @Column(name="movieCd", nullable=false)
    private String movieCd;
    
    @Column(name="rank",nullable=true)
    private String rank;
    
    @Column(name = "movie_nm",nullable=true)
    private String movieNm;

    @Column(name = "openDt",nullable=true)
    private String openDt;

    @Column(name = "poster_path",nullable=true)
    private String posterPath;

    @Column(name = "overview", length = 2000,nullable=true)
    public String overview;
    
    @Column(name="showtime", nullable=true)
    private String showtime;

    @Column(name = "tmdb_id",nullable=true)
    private Integer tmdbId;
    
    //상영타임 및 상세정보에 나와야할 컬럼 다넣기
    // Getters and setters
}

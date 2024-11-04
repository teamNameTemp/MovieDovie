package com.board.entity;

import java.util.List;

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
@Entity(name="movie")
@Table(name = "tbl_movie")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //영화코드
    @Column(name="moviecd", nullable=false)
    private String movieCd;
    
    //순위
    @Column(name="rank",nullable=true)
    private String rank;
    
    //이름
    @Column(name = "movienm",nullable=true)
    private String movieNm;

    //개봉일
    @Column(name = "opendt",nullable=true)
    private String openDt;

    //이미지경로
    @Column(name = "poster_path",nullable=true)
    private String posterPath;

    //줄거리
    @Column(name = "overview", length = 2000,nullable=true)
    public String overview;
    
    //상영시간
    @Column(name="showtime", nullable=true)
    private String showtime;

    //tmdbid
    @Column(name = "tmdb_id",nullable=true)
    private Integer tmdbId;
    
    @Column(name="directors", nullable=true)
    private List<DirectorEntity> directors;
    
    //관리자가 업로드한 이미지
    @Column(name="img_path", nullable=true)
    private String imgPath;
    
    @Column(name="actors",nullable=true)
    private List<ActorEntity> actors;
    
    //조회(hitno)
    @Column(name="hitno",nullable=false)
    private Long hitno;
    //매출액
    @Column(name="sales_acc",nullable=true)
    private Long salesAcc;
    //관객수
    @Column(name="audi_acc",nullable=true)
    private Long audiacc;
    //업로드여부(null)
    @Column(name="uploadcheck",nullable=false)
    private String uploadCheck;
    
}

package com.board.entity.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="videos")
@Table(name = "jpa_video")
@Data
@NoArgsConstructor
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    // 언어 코드 (ISO 639-1)
    @Column(name="iso_639_1", nullable = true)
	private String iso639_1;
    // 국가 코드 (ISO 3166-1)
    @Column(name="iso_3166_1")
	private String iso3166_1;
    // 비디오 키 영상경로!
    @Column(name="key", nullable = true)
	private String key;
	// 비디오 이름
    @Column(name="name" ,nullable = true)
	private String name;
	// 비디오 사이트 (예: YouTube)
    @Column(name="site",nullable = true)
    private String site;
	// 비디오 크기
    @Column(name="size",nullable = true)
	private int size;
	// 비디오 유형
    @Column(name="type",nullable = true)
	private String type;
    
//    @ManyToOne
//    @JoinColumn(name = "movie_id")
//    private MovieEntity movie;
}
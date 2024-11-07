package com.board.entity.movie;

import java.util.ArrayList;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "movie")
@Table(name = "jpa_movie")
@DynamicInsert
public class MovieEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")
	@SequenceGenerator(name = "MOVIE_SEQ", sequenceName = "tbl_movie_seq", initialValue = 1, allocationSize = 1)
	private long seqno;

	// 영화코드
	@Column(name = "moviecd", nullable = true)
	private String movieCd;

	// 순위
	@Column(name = "rank", nullable = true)
	private String rank;

	// 이름
	@Column(name = "movie_nm", nullable = true)
	private String movieNm;
	// 이름(영문)
	@Column(name = "movie_nmen", nullable = true)
	private String movieNmEn;

	// 제작연도
	@Column(name = "prdt_year", nullable = true)
	private String prdtYear;
	// 제작국가
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NationEntity> nations = new ArrayList<>();

	// 제작사
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CompanyEntity> companys = new ArrayList<>();

	// 장르
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GenreEntity> genres = new ArrayList<>();

	// 배우
	@Column(nullable=true)
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ActorEntity> actors = new ArrayList<>();

	// 관람
	@Column(name="watch_grade_nm",nullable=true)
	private String watchGradeNm;
	
	// 개봉일
	@Column(name = "opendt", nullable = true)
	private String openDt;

	// 이미지경로
	@Column(name = "poster_path", nullable = true)
	private String posterPath;

	// 줄거리
	@Column(name = "overview", length = 2000, nullable = true)
	public String overview;

	// 상영시간
	@Column(name = "showtime", nullable = true)
	private String showtime;

	// 감독
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DirectorEntity> directors = new ArrayList<>();

	// 관리자가 업로드한 이미지
	@Column(name = "img_path", nullable = true)
	private String imgPath;

	// 조회(hitno)
	@Column(name = "hitno", nullable = true)
	@ColumnDefault("0")
	private Integer hitno;
	// 매출액
	@Column(name = "sales_acc", nullable = true)
	private String salesAcc;
	// 관객수
	@Column(name = "audi_acc", nullable = true)
	private String audiAcc;
	// 업로드여부(null)
	@Column(name = "uploadcheck", nullable = true)
	private String uploadCheck;

	public void addNations(NationEntity entity) {
		if (entity != null) {
			nations.add(entity);
			entity.setMovie(this);
		}
	}

	public void addGenres(GenreEntity entity) {
		if (entity != null) {
			genres.add(entity);
			entity.setMovie(this);
		}
	}

	public void addDirectors(DirectorEntity entity) {
		if (entity != null) {
			directors.add(entity);
			entity.setMovie(this);
		}
	}

	public void addActors(ActorEntity entity) {
		if (entity != null) {
			actors.add(entity);
			entity.setMovie(this);
		}
	}

	public void addCompanys(CompanyEntity entity) {
		if (entity != null) {
			companys.add(entity);
			entity.setMovie(this);
		}
	}

}

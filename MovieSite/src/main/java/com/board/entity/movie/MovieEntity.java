package com.board.entity.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "movie")
@Table(name = "jpa_movie")
@DynamicInsert
public class MovieEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MOVIE_SEQ")
	@SequenceGenerator(name="MOVIE_SEQ", sequenceName = "tbl_movie_seq", initialValue = 1, allocationSize = 1)
	private long seqno;
	
	// 영화코드
	@Column(name = "moviecd", nullable = false)
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
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_nation", 
	joinColumns = @JoinColumn(name = "movie_id"), 
	inverseJoinColumns = @JoinColumn(name = "nation_id"))
	private List<NationEntity> nations = new ArrayList<>();

	// 제작사
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_company", 
	joinColumns = @JoinColumn(name = "movie_id"), 
	inverseJoinColumns = @JoinColumn(name = "company_id"))
	private List<CompanyEntity> companys = new ArrayList();

	// 장르
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_genre", 
	joinColumns = @JoinColumn(name = "movie_id"), 
	inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private List<GenreEntity> genres = new ArrayList();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_actor", 
	joinColumns = @JoinColumn(name = "movie_id", nullable=true), 
	inverseJoinColumns = @JoinColumn(name = "actor_id",nullable=true))
	private List<ActorEntity> actors = new ArrayList();

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

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_director", 
	joinColumns = @JoinColumn(name = "movie_id"), 
	inverseJoinColumns = @JoinColumn(name = "director_id"))	private List<DirectorEntity> directors;

	// 관리자가 업로드한 이미지
	@Column(name = "img_path", nullable = true)
	private String imgPath;

	//조회(hitno)
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

}

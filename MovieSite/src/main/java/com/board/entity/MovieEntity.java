package com.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="movie")
@Table(name="jpa_movie")
public class MovieEntity {
	
	@Id
	//기본키 생성 전략 : uuid, table, sequence, auto, identity, 
	/*
	 - sequence : Oracle의 시퀀스 전략을 채택
	 - auto : 사용하는 RDBMS의 정책을 따름. 
	 - identity : My-SQL에서 auto_increment 사용 
	 - table : sequence 랑 비슷. hibernate 작동을 보면 내부 작동 시에 SQL을 만들어 만듬.
	    테이블을 참조하는 방법이 틀림.
	 - uuid : insert시에 기본키 값을 중복이 안되도록 난수 발생 
	 */
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ")	
	@SequenceGenerator(name="MOVIE_SEQ", sequenceName = "jpa_movie_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="seqno", nullable=false)
	private Long seqno; //스프링JPA에서 기본키가 숫자인 경우 반드시 데이터 타입은 long이어야 함.

	@Column(name="code", nullable=true)
	private Long code;

	@Column(name="title", length=200, nullable=false)
	private String title;
	
	@Column(name="titleen", length=200, nullable=false)
	private String titleen; 
	
	@Column(name="prodtyear", nullable=false)
	private LocalDateTime prodtyear;
	
	@Column(name="nationalt", length=50, nullable=false)
	private String nationalt;
	
	@Column(name="showtime", nullable=true)
	private Long showtime;
	
	@Column(name="genres", length=2000, nullable=false)
	private String genres;
	
	@Column(name="directors", length=200, nullable=false)
	private String directors;
	
	@Column(name="companynm", length=100, nullable=true)
	private String companynm;
	
	@Column(name="viewcnt", nullable=true)
	private Long viewcnt;
	
	@Column(name="likecnt", nullable=true)
	private Long likecnt;
	
	@Column(name="dislikecnt", nullable=true)
	private Long dislikecnt;
	
	@Column(name="rentcnt", nullable=true)
	private Long rentcnt;
	
	@Column(name="uploadcheck", length=1, nullable=true)
	private String uploadcheck;
	
	@Column(name="hitno", nullable=false)
	private Long hitno;

	@Column(name="actors", length=2000, nullable=false)
	private String actors;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email", nullable = false)
	private MemberEntity email;
	
}

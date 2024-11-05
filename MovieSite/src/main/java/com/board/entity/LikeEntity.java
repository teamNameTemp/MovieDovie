package com.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name="like")
@Table(name="tbl_like")
public class LikeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIKE_SEQ")
	@SequenceGenerator(name="LIKE_SEQ", sequenceName = "jpa_like_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name = "likecheck", length=1, nullable = true)
	private String likecheck;
	
	@Column(name = "dislikecheck", length=1, nullable = true)
	private String dislikecheck;
	
	@Column(name = "email", length=320, nullable = true)
	private String email;
	
	@Column(name="movieseqno", nullable = true)
	private Long movieseqno;
	
	
	/*
	//FK 만들기
	//FK 읽어올 때 Eager, Lazy 두 가지 타입이 있음.
	//Eager : 부모키가 있는 테이블부터 검사해서 부모키가 제대로 되어 있는지 확인하고 자식키를 읽음 -> 정확도는 높지만 성능이 저하.
	//Lazy : 자식키가 있는 테이블만 읽음 -> 정확도는 떨어지지만 성능이 향상.
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email", nullable = false)
	private MemberEntity email;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "movieseqno", referencedColumnName = "seqno", nullable = false)
	private MovieEntity movieseqno;
	*/
	
	
}

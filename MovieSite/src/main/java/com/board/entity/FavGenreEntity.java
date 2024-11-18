package com.board.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="favGenre")
@Table(name="favGenre")
public class FavGenreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAV_GENRE_SEQ")
	@SequenceGenerator(name="FAV_GENRE_SEQ", sequenceName = "fav_genre_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name = "genre1", length=50, nullable = true)
	private String genre1;
	
	@Column(name = "genre2", length=50, nullable = true)
	private String genre2;
	
	@Column(name = "genre3", length=50, nullable = true)
	private String genre3;
	
	
	@Column(name = "email", length=320, nullable = false)
	private String email;
	
	
	/*
	//FK 만들기
	//FK 읽어올 때 Eager, Lazy 두 가지 타입이 있음.
	//Eager : 부모키가 있는 테이블부터 검사해서 부모키가 제대로 되어 있는지 확인하고 자식키를 읽음 -> 정확도는 높지만 성능이 저하.
	//Lazy : 자식키가 있는 테이블만 읽음 -> 정확도는 떨어지지만 성능이 향상.
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email", nullable = false)
	private MemberEntity email;
	*/
	
}

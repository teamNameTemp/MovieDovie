package com.board.entity;

import java.time.LocalDateTime;

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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="watch")
@Table(name="tbl_watch")
public class WatchEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WATCH_SEQ")
	@SequenceGenerator(name="WATCH_SEQ", sequenceName = "jpa_watch_seq", initialValue = 1, allocationSize = 1)
	private Long seqno;
	
	@Column(name = "status", length=2, nullable = true)
	private String status;
	
	@Column(name="watchdate", nullable=true)
	private LocalDateTime watchdate;
	
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

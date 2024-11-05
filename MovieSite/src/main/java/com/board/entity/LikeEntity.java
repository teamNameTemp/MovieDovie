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
	private MemberEntity email;

	@Column(name="movieseqno", nullable = true)
	private MovieEntity movieseqno;

}	


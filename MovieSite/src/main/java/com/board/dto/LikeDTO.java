package com.board.dto;

import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.MovieEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDTO {
	private Long seqno;
	private String likecheck;
	private String dislikecheck;
    private MemberEntity email;
    private MovieEntity movieseqno;
	
	//Entity --> DTO 이동
	public LikeDTO(LikeEntity likeEntity) {
		this.seqno = likeEntity.getSeqno();
		this.likecheck = likeEntity.getLikecheck();
		this.dislikecheck = likeEntity.getDislikecheck();
		this.email = likeEntity.getEmail();
		this.movieseqno = likeEntity.getMovieseqno();
	}
	
	//DTO --> Entity 이동
	public LikeEntity dtoToEntity(LikeDTO dto) {
		
		LikeEntity likeEntity = LikeEntity.builder()
										.seqno(dto.getSeqno())
										.likecheck(dto.getLikecheck())
										.dislikecheck(dto.getDislikecheck())
										.email(dto.getEmail())
										.movieseqno(dto.getMovieseqno())
										.build();									
		return likeEntity;
	}
	
}

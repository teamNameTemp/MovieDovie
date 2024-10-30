package com.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeDTO {
	private Long seqno;
	private String likecheck;
	private String dislikecheck;
	private String email;
	private Long movieseqno;
}

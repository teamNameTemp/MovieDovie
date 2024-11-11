package com.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FavGenreDTO {
	private Long seqno;
	private String genre1;
	private String genre2;
	private String genre3;
	private String email;
}

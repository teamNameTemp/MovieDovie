package com.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookmarkDTO {
	private Long seqno;
	private String status;
	private String email;
	private Long movieseqno;
}

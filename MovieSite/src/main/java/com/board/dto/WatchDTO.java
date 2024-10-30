package com.board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WatchDTO {
	private Long seqno;
	private String status;
	private LocalDateTime watchdate;
	private String email;
	private Long movieseqno;
}

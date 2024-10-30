package com.board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestDTO {
	private Long seqno;
	private String title;
	private LocalDateTime reqdate;
	private String status;
	private String content;
	private String email;
}

package com.board.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

	private Long replyseqno;
	private Long seqno;
	private Long movieseqno;  
	private String email;
	private String replywriter;
	private String replycontent;
	private LocalDateTime replyregdate;
}

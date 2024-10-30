package com.board.dto;

import java.time.LocalDateTime;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberLogDTO {
	private Long seqno;
	private LocalDateTime inouttime;
	private String status;
	private String email;
}

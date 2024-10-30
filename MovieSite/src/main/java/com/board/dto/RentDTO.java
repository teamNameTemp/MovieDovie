package com.board.dto;

import java.time.LocalDateTime;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RentDTO {
	private Long seqno;
	private LocalDateTime rentdate;
	private LocalDateTime returndate;
	private String status;
	private String email;
	private Long movieseqno;
}

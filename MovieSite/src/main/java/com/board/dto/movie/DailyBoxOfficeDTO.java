package com.board.dto.movie;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DailyBoxOfficeDTO {
	private Long seqno;
	private String today;
	private Long rank;
	private String movieNm;
	private LocalDateTime openDt;
	private String posterPath;
	private String overview;
}

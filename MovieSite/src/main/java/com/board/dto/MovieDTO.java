package com.board.dto;

import java.time.LocalDateTime;

import com.board.entity.MovieEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
	
	private Long seqno;
	private Long code;
	private String title; 
	private String titleen;
	private LocalDateTime prodtyear;
	private String nationalt; 
	private Long showtime;
	private String genres; 
	private String directors; 
	private String companynm; 
	private String uploadcheck; 
	private Long hitno;
	private String actors;
}

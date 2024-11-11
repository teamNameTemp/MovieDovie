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
	private Long viewcnt;
	private Long likecnt;
	private Long dislikecnt;
	private Long rentcnt;
	private String uploadcheck; 
	private Long hitno;
	private String actors;
	
	//Entity --> DTO 이동
	public MovieDTO(MovieEntity movieEntity) {
		this.seqno = movieEntity.getSeqno();
		this.code = movieEntity.getCode();
		this.title = movieEntity.getTitle();
		this.titleen = movieEntity.getTitleen();
		this.prodtyear = movieEntity.getProdtyear();
		this.nationalt = movieEntity.getNationalt();
		this.showtime = movieEntity.getShowtime();
		this.genres = movieEntity.getGenres();
		this.directors = movieEntity.getDirectors();
		this.companynm = movieEntity.getCompanynm();
		this.viewcnt = movieEntity.getViewcnt();
		this.likecnt = movieEntity.getLikecnt();
		this.dislikecnt = movieEntity.getDislikecnt();
		this.rentcnt = movieEntity.getRentcnt();
		this.uploadcheck = movieEntity.getUploadcheck();
		this.hitno = movieEntity.getHitno();
		this.actors = movieEntity.getActors();
	}
	
	//DTO --> Entity 이동
	public MovieEntity dtoToEntity(MovieDTO dto) {
		
		MovieEntity movieEntity = MovieEntity.builder()
											 .seqno(dto.getSeqno())
											 .code(dto.getCode())
											 .title(dto.getTitle())
											 .titleen(dto.getTitleen())
											 .prodtyear(dto.getProdtyear())
											 .nationalt(dto.getNationalt())
											 .showtime(dto.getShowtime())
											 .genres(dto.getGenres())
											 .directors(dto.getDirectors())
											 .companynm(dto.getCompanynm())
											 .viewcnt(dto.getViewcnt())
											 .likecnt(dto.getLikecnt())
											 .dislikecnt(dto.getDislikecnt())
											 .rentcnt(dto.getRentcnt())
											 .uploadcheck(dto.getUploadcheck())
											 .hitno(dto.getHitno())
											 .actors(dto.getActors())
											 .build();
							
		return movieEntity;
	}
}

package com.board.dto;

import com.board.entity.BookmarkEntity;
import com.board.entity.LikeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkDTO {
	private Long seqno;
	private String status;
	private String email;
	private Long movieseqno;
	
	//Entity --> DTO 이동
	public BookmarkDTO(BookmarkEntity bookmarkEntity) {
		this.seqno = bookmarkEntity.getSeqno();
		this.status = bookmarkEntity.getStatus();
		this.email = bookmarkEntity.getEmail();
		this.movieseqno = bookmarkEntity.getMovieseqno();
	}
	
	//DTO --> Entity 이동
	public BookmarkEntity dtoToEntity(BookmarkDTO dto) {
		
		BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
										.seqno(dto.getSeqno())
										.status(dto.getStatus())
										.email(dto.getEmail())
										.movieseqno(dto.getMovieseqno())
										.build();
		return bookmarkEntity;
	}

	
}

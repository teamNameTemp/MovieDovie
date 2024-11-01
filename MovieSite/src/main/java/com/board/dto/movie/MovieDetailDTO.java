package com.board.dto.movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDetailDTO {
    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    
    // 추가 필드...
    
    // Getters and setters
}
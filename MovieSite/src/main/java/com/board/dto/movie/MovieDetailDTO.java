package com.board.dto.movie;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MovieDetailDTO {
    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private List<DirectorDetailDTO> directors;
    private List<ActorDetailDTO> actors;
}